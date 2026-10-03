package com.trickymind.game.core

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(
            "tricky_mind_prefs",
            Context.MODE_PRIVATE
        )

    var isSoundEnabled: Boolean
        get() = prefs.getBoolean("sound_enabled", true)
        set(value) {
            prefs.edit()
                .putBoolean("sound_enabled", value)
                .apply()
        }

    var isMusicEnabled: Boolean
        get() = prefs.getBoolean("music_enabled", true)
        set(value) {
            prefs.edit()
                .putBoolean("music_enabled", value)
                .apply()
        }

    var isHapticEnabled: Boolean
        get() = prefs.getBoolean("haptic_enabled", true)
        set(value) {
            prefs.edit()
                .putBoolean("haptic_enabled", value)
                .apply()
        }

    var isPremiumUser: Boolean
        get() = prefs.getBoolean("is_premium", false)
        set(value) {
            prefs.edit()
                .putBoolean("is_premium", value)
                .apply()
        }

    fun getHighScore(gameId: String): Int {
        return prefs.getInt(
            "high_score_$gameId",
            0
        )
    }

    fun setHighScore(
        gameId: String,
        score: Int
    ) {
        val current = getHighScore(gameId)

        if (score > current) {
            prefs.edit()
                .putInt(
                    "high_score_$gameId",
                    score
                )
                .apply()
        }
    }
}
