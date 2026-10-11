package com.deenora.app.ads

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.deenora.app.BuildConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

private const val TEST_BANNER_AD_UNIT_ID =
    "ca-app-pub-3940256099942544/9214589741"

// This production ad unit is configured in the linked AdMob account.
// AdMob may still withhold inventory until app/store review is complete.
private const val PRODUCTION_BANNER_AD_UNIT_ID =
    "ca-app-pub-5225037816508060/4090203029"

@Composable
fun DeenoraBannerAd(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val adView = remember {
        AdView(context).apply {
            setAdSize(
                AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    context,
                    360
                )
            )

            adUnitId = if (BuildConfig.USE_TEST_ADS) {
                TEST_BANNER_AD_UNIT_ID
            } else {
                PRODUCTION_BANNER_AD_UNIT_ID
            }
        }
    }

    DisposableEffect(adView) {
        adView.loadAd(AdRequest.Builder().build())

        onDispose {
            adView.destroy()
        }
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = {
            adView
        }
    )
}
