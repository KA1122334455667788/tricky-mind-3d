package com.trickymind.game.core

import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.os.Build

class AudioManager(
    private val context: Context,
    private val prefsManager: PrefsManager
) {

    private var musicPlayer: MediaPlayer? = null

    fun playClick() {
        if (!prefsManager.isSoundEnabled) return
        playSound(R.raw.click)
    }

    fun playSuccess() {
        if (!prefsManager.isSoundEnabled) return
        playSound(R.raw.success)
    }

    fun playGameOver() {
        if (!prefsManager.isSoundEnabled) return
        playSound(R.raw.gameover)
    }

    private fun playSound(resourceId: Int) {
        try {
            val player = MediaPlayer.create(context, resourceId)

            player?.setOnCompletionListener {
                it.release()
            }

            player?.start()
        } catch (_: Exception) {
        }
    }

    fun startMusic() {
        if (!prefsManager.isMusicEnabled) return

        try {
            if (musicPlayer == null) {
                musicPlayer = MediaPlayer.create(
                    context,
                    R.raw.bg_music
                )

                musicPlayer?.isLooping = true
            }

            if (musicPlayer?.isPlaying != true) {
                musicPlayer?.start()
            }
        } catch (_: Exception) {
        }
    }

    fun pauseMusic() {
        try {
            if (musicPlayer?.isPlaying == true) {
                musicPlayer?.pause()
            }
        } catch (_: Exception) {
        }
    }

    fun resumeMusic() {
        if (!prefsManager.isMusicEnabled) return

        try {
            if (musicPlayer != null &&
                musicPlayer?.isPlaying
