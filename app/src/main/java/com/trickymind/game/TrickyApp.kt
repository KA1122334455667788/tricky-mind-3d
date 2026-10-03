package com.trickymind.game

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class TrickyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val requestConfiguration =
            RequestConfiguration.Builder()
                .setTagForChildDirectedTreatment(
                    RequestConfiguration
                        .TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
                )
                .setMaxAdContentRating(
                    RequestConfiguration
                        .MAX_AD_CONTENT_RATING_G
                )
                .build()

        MobileAds.setRequestConfiguration(
            requestConfiguration
        )

        MobileAds.initialize(this)
    }
}
