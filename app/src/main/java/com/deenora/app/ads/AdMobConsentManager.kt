package com.deenora.app.ads

import android.app.Activity
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform

object AdMobConsentManager {

    private var notified = false

    fun requestConsentAndInitialize(
        activity: Activity,
        onAdsReady: () -> Unit = {}
    ) {
        val consentInformation =
            UserMessagingPlatform.getConsentInformation(activity)

        val params = ConsentRequestParameters.Builder()
            .build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) {
                    notifyIfReady(consentInformation, onAdsReady)
                }
            },
            {
                notifyIfReady(consentInformation, onAdsReady)
            }
        )
    }

    private fun notifyIfReady(
        consentInformation: ConsentInformation,
        onAdsReady: () -> Unit
    ) {
        if (!notified && consentInformation.canRequestAds()) {
            notified = true
            onAdsReady()
        }
    }
}
