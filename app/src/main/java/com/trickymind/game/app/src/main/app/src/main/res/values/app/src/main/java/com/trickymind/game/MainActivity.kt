package com.trickymind.game

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trickymind.game.game.BlockPuzzleEngineView
import com.trickymind.game.game.GameEventListener

class MainActivity : AppCompatActivity(), GameEventListener {

    private lateinit var prefsManager: com.trickymind.game.core.PrefsManager
    private lateinit var audioManager: com.trickymind.game.core.AudioManager

    private var gameView: BlockPuzzleEngineView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefsManager =
            com.trickymind.game.core.PrefsManager(this)

        audioManager =
            com.trickymind.game.core.AudioManager(
                this,
                prefsManager
            )

        showHome()
    }

    private fun showHome() {

        gameView = null

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(40, 40, 40, 40)
        }

        val title = TextView(this).apply {
            text = "TRICKY MIND 3D"
            textSize = 30f
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Block Puzzle"
            textSize = 20f
            gravity = Gravity.CENTER
        }

        val playButton = Button(this).apply {
            text = "PLAY BLOCK PUZZLE"

            setOnClickListener {
                audioManager.playClick()
                startBlockPuzzle()
            }
        }

        layout.addView(
            title,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        layout.addView(
            subtitle,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 20
                bottomMargin = 40
            }
        )

        layout.addView(
            playButton,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        setContentView(layout)
    }

    private fun startBlockPuzzle() {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val homeButton = Button(this).apply {
            text = "HOME"

            setOnClickListener {
                audioManager.playClick()
                showHome()
            }
        }
