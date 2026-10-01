package com.deenora.app.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri

object PrivacyPolicyHelper {
    const val URL = "https://nizzarr-aj.github.io/DEENORA/privacy-policy.html"

    fun open(context: Context) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(URL))
        )
    }
}
