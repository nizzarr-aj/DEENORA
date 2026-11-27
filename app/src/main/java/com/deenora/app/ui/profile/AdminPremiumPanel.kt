package com.deenora.app.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val ADMIN_FUNCTION_URL = "https://iujslmvuyundjxbkkkon.supabase.co/functions/v1/admin-premium"
private const val SUPABASE_PUBLIC_KEY = "sb_publishable_6AlIaFNnew-_-2Y-gCPeXA_Xdn0eGmo"

@Composable
fun AdminPremiumPanel(accessToken: String, onClose: () -> Unit) {
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var requests by remember { mutableStateOf(JSONArray()) }
    var subscriptions by remember { mutableStateOf(JSONArray()) }

    suspend fun call(action: String, id: String = "", decision: String = ""): JSONObject =
        withContext(Dispatchers.IO) {
            val conn = (URL(ADMIN_FUNCTION_URL).openConnection() as HttpURLConnection)
            try {
                conn.requestMethod = "POST"
                conn.connectTimeout = 15000
                conn.readTimeout = 20000
                conn.setRequestProperty("Content-Type", "application/json")
                conn.setRequestProperty("apikey", SUPABASE_PUBLIC_KEY)
                conn.setRequestProperty("Authorization", "Bearer $accessToken")
                conn.doOutput = true
                val body = JSONObject().put("action", action)
                if (id.isNotBlank()) body.put("request_id", id)
                if (decision.isNotBlank()) body.put("decision", decision)
                conn.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
                val code = conn.responseCode
                val stream = if (code in 200..299) conn.inputStream else conn.errorStream
                val text = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
                val json = JSONObject(text.ifBlank { "{}" })
                if (code !in 200..299) throw IllegalStateException(json.optString("error", "Request failed (HTTP $code)"))
                json
            } finally { conn.disconnect() }
        }

    fun refresh() {
        scope.launch {
            loading = true
            error = ""
            try {
                val data = call("list")
                requests = data.optJSONArray("requests") ?: JSONArray()
                subscriptions = data.optJSONArray("subscriptions") ?: JSONArray()
            } catch (e: Exception) { error = e.message ?: "Could not load admin data." }
            finally { loading = false }
        }
    }
    LaunchedEffect(accessToken) { refresh() }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Deenora • Admin Premium") },
        text = {
            Column(Modifier.fillMaxWidth().heightIn(max = 520.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { refresh() }, label = { Text("Refresh") })
                    Text("Requests: ${requests.length()}  •  Subscriptions: ${subscriptions.length()}", style = MaterialTheme.typography.bodySmall)
                }
                if (loading) LinearProgressIndicator(Modifier.fillMaxWidth())
                if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
                if (!loading && requests.length() == 0 && error.isBlank()) Text("No payment requests yet.")
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(requests.length()) { index ->
                        val item = requests.optJSONObject(index) ?: JSONObject()
                        val id = item.optString("id")
                        val status = item.optString("status")
                        Card(shape = RoundedCornerShape(12.dp)) {
                            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text("${item.optString("plan_code").uppercase()} • ${status.uppercase()}", fontWeight = FontWeight.Bold)
                                Text("User ID: ${item.optString("user_id")}", style = MaterialTheme.typography.bodySmall)
                                Text("Amount: ${item.optString("amount")} ${item.optString("currency")}")
                                Text("Reference: ${item.optString("payment_reference")}")
                                if (status == "pending") {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(onClick = {
                                            scope.launch {
                                                loading = true; error = ""
                                                try { call("review", id, "approved"); refresh() }
                                                catch (e: Exception) { error = e.message ?: "Approval failed"; loading = false }
                                            }
                                        }, enabled = !loading) { Text("Approve") }
                                        OutlinedButton(onClick = {
                                            scope.launch {
                                                loading = true; error = ""
                                                try { call("review", id, "rejected"); refresh() }
                                                catch (e: Exception) { error = e.message ?: "Rejection failed"; loading = false }
                                            }
                                        }, enabled = !loading) { Text("Reject") }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onClose) { Text("Close") } }
    )
}
