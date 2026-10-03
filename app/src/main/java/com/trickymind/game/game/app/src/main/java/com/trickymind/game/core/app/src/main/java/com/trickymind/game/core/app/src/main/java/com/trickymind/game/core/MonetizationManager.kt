package com.trickymind.game.core

import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class MonetizationManager(
    private val context: Context,
    private val prefsManager: PrefsManager
) {

    private var bannerAdView: AdView? = null

    fun loadBanner(container: ViewGroup) {

        if (prefsManager.isPremiumUser) {
            container.removeAllViews()
            return
        }

        try {
            if (bannerAdView == null) {
                bannerAdView = AdView(context).apply {

                    setAdSize(AdSize.BANNER)

                    // Google test Banner Ad Unit ID
                    // Replace with your real AdMob ID before production.
                    adUnitId =
                        "ca-app-pub-3940256099942544/6300978111"
                }
            }

            container.removeAllViews()
            container.addView(bannerAdView)

            val adRequest = AdRequest.Builder().build()
            bannerAdView?.loadAd(adRequest)

        } catch (_: Exception) {
        }
    }

    fun pauseBanner() {
        try {
            bannerAdView?.pause()
        } catch (_: Exception) {
        }
    }

    fun resumeBanner() {
        try {
            bannerAdView?.resume()
        } catch (_: Exception) {
        }
    }

    fun destroyBanner() {
        try {
            bannerAdView?.destroy()
            bannerAdView = null
        } catch (_: Exception) {
            bannerAdView = null
        }
    }
