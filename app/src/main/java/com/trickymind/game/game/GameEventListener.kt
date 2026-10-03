package com.trickymind.game.game

interface GameEventListener {

    fun onScoreChanged(newScore: Int)

    fun onGameOver(finalScore: Int)

    fun onGameWon(finalScore: Int)

    fun onRequestSoundClick()

    fun onRequestSoundSuccess()

    fun onRequestSoundGameOver()

    fun onRequestVibrate(durationMillis: Long)
}
