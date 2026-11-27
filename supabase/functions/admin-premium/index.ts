import "jsr:@supabase/functions-js/edge-runtime.d.ts";

const SUPABASE_URL = Deno.env.get("SUPABASE_URL")!;
const ANON_KEY = Deno.env.get("SUPABASE_ANON_KEY")!;
const SERVICE_ROLE_KEY = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!;
const ADMIN_EMAIL = "nizarajala3@gmail.com";

const cors = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Headers": "authorization, x-client-info, apikey, content-type",
  "Access-Control-Allow-Methods": "POST, OPTIONS",
  "Content-Type": "application/json",
};

function reply(status: number, body: unknown) {
  return new Response(JSON.stringify(body), { status, headers: cors });
}

async function supabase(path: string, init: RequestInit = {}) {
  return fetch(`${SUPABASE_URL}/rest/v1/${path}`, {
    ...init,
    headers: {
      apikey: SERVICE_ROLE_KEY,
      Authorization: `Bearer ${SERVICE_ROLE_KEY}`,
      "Content-Type": "application/json",
      ...(init.headers || {}),
    },
  });
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") return new Response("ok", { headers: cors });
  if (req.method !== "POST") return reply(405, { error: "POST required" });

  const authorization = req.headers.get("Authorization") || "";
  const token = authorization.replace(/^Bearer\s+/i, "");
  if (!token) return reply(401, { error: "Sign in with a Supabase account first." });

  // Validate the caller's Supabase JWT with Auth; never trust an email supplied in the request.
  const userResponse = await fetch(`${SUPABASE_URL}/auth/v1/user`, {
    headers: { apikey: ANON_KEY, Authorization: `Bearer ${token}` },
  });
  if (!userResponse.ok) return reply(401, { error: "Your session is invalid or expired." });
  const user = await userResponse.json();
  if (String(user.email || "").toLowerCase() !== ADMIN_EMAIL) {
    return reply(403, { error: "Admin access denied." });
  }

  let body: { action?: string; request_id?: string; decision?: string; note?: string };
  try { body = await req.json(); } catch { return reply(400, { error: "Invalid JSON." }); }

  if (body.action === "list") {
    const [requestsResponse, subscriptionsResponse] = await Promise.all([
      supabase("premium_payment_requests?select=*&order=submitted_at.desc&limit=200"),
      supabase("premium_subscriptions?select=*&order=created_at.desc&limit=200"),
    ]);
    if (!requestsResponse.ok || !subscriptionsResponse.ok) {
      return reply(502, { error: "Could not load premium records." });
    }
    const requests = await requestsResponse.json();
    const subscriptions = await subscriptionsResponse.json();
    return reply(200, {
      requests,
      subscriptions,
      stats: {
        pending: requests.filter((r: any) => r.status === "pending").length,
        approved: requests.filter((r: any) => r.status === "approved").length,
        rejected: requests.filter((r: any) => r.status === "rejected").length,
        activeSubscriptions: subscriptions.filter((s: any) => s.status === "active" && new Date(s.expires_at) > new Date()).length,
      },
    });
  }

  if (body.action === "review") {
    if (!body.request_id || !["approved", "rejected"].includes(body.decision || "")) {
      return reply(400, { error: "request_id and decision approved/rejected are required." });
    }
    const getResponse = await supabase(
      `premium_payment_requests?id=eq.${encodeURIComponent(body.request_id)}&select=*&limit=1`,
    );
    if (!getResponse.ok) return reply(502, { error: "Could not read payment request." });
    const rows = await getResponse.json();
    const payment = rows[0];
    if (!payment) return reply(404, { error: "Payment request not found." });
    if (payment.status !== "pending") return reply(409, { error: "This request has already been reviewed." });

    const reviewedAt = new Date().toISOString();
    const updateResponse = await supabase(
      `premium_payment_requests?id=eq.${encodeURIComponent(body.request_id)}&status=eq.pending`,
      {
        method: "PATCH",
        headers: { Prefer: "return=representation" },
        body: JSON.stringify({
          status: body.decision,
          reviewed_at: reviewedAt,
          reviewed_by: user.id,
          reviewer_note: (body.note || "").slice(0, 500),
        }),
      },
    );
    if (!updateResponse.ok) return reply(502, { error: "Could not update payment request." });
    const updated = await updateResponse.json();
    if (!updated.length) return reply(409, { error: "Request was reviewed by another admin action." });

    if (body.decision === "approved") {
      const durationDays = payment.plan_code === "yearly" ? 365 : 30;
      const startsAt = new Date();
      const expiresAt = new Date(startsAt.getTime() + durationDays * 24 * 60 * 60 * 1000);
      const subResponse = await supabase("premium_subscriptions?on_conflict=payment_request_id", {
        method: "POST",
        headers: { Prefer: "resolution=merge-duplicates,return=representation" },
        body: JSON.stringify({
          user_id: payment.user_id,
          plan_code: payment.plan_code,
          starts_at: startsAt.toISOString(),
          expires_at: expiresAt.toISOString(),
          status: "active",
          payment_request_id: payment.id,
        }),
      });
      if (!subResponse.ok) {
        return reply(502, { error: "Payment was approved, but subscription creation failed. Please retry/support." });
      }
    }
    return reply(200, { request: updated[0], message: body.decision === "approved" ? "Approved and subscription activated." : "Request rejected." });
  }

  return reply(400, { error: "Unknown action. Use list or review." });
});
