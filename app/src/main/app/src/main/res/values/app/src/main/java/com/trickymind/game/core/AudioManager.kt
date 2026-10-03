package com.trickymind.game.core

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class AudioManager(
    private val context: Context,
    private val prefsManager: PrefsManager
) {

    fun playClick() {
        // Sound disabled until audio files are added.
    }

    fun playSuccess() {
        // Sound disabled until audio files are added.
    }

    fun playGameOver() {
        // Sound disabled until audio files are added.
    }

    fun startMusic() {
        // Music disabled until bg_music is added.
    }

    fun pauseMusic() {
        // Music disabled until bg_music is added.
    }

    fun resumeMusic() {
        // Music disabled until bg_music is added.
    }

    fun stopMusic() {
        // Music disabled until bg_music is added.
    }

    fun vibrate(durationMillis: Long) {
        if (!prefsManager.isHapticEnabled) return

        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE)
                        as VibratorManager

                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        durationMillis,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(durationMillis)
            }

        } catch (_: Exception) {
        }
    }

    fun release() {
        // Nothing to release while audio is disabled.
    }
}
