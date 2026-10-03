package com.trickymind.game.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import kotlin.math.min
import kotlin.random.Random

class BlockPuzzleEngineView(
    context: Context,
    private val listener: GameEventListener
) : View(context) {

    companion object {
        private const val GRID_SIZE = 8
        private const val WIN_SCORE = 500
        private const val CELL_PADDING = 4f
    }

    private val grid = Array(GRID_SIZE) { IntArray(GRID_SIZE) }

    private var score = 0
    private var isFinished = false

    private val cellPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#424242")
        strokeWidth = 2f
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
    }

    private val colors = intArrayOf(
        Color.TRANSPARENT,
        Color.parseColor("#FF5252"),
        Color.parseColor("#FFCA28"),
        Color.parseColor("#66BB6A"),
        Color.parseColor("#42A5F5"),
        Color.parseColor("#AB47BC"),
        Color.parseColor("#FF7043")
    )

    private val shapes = arrayOf(
        arrayOf(
            intArrayOf(0, 0)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(1, 0)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0),
            intArrayOf(1, 1)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1),
            intArrayOf(0, 2)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(1, 0),
            intArrayOf(2, 0)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 0)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1),
            intArrayOf(1, 1)
        ),

        arrayOf(
            intArrayOf(0, 0),
            intArrayOf(0, 1),
            intArrayOf(0, 2),
            intArrayOf(1, 1)
        )
    )

    private var currentShape: Array<IntArray> = shapes[0]
    private var currentColor = 1

    init {
        resetGame()
    }

    fun resetGame() {
        for (r in 0 until GRID_SIZE) {
            grid[r].fill(0)
        }

        score = 0
        isFinished = false

        generateNextShape()

        listener.onScoreChanged(score)
        invalidate()
    }

    private fun generateNextShape() {
        currentShape = shapes[Random.nextInt(shapes.size)]
        currentColor = Random.nextInt(1, colors.size)

        if (!hasAnyValidPlacement()) {
            finishGame()
        }
    }

    private fun canPlaceShape(
        shape: Array<IntArray>,
        startRow: Int,
        startCol: Int
    ): Boolean {

        for (cell in shape) {
            val row = startRow + cell[0]
            val col = startCol + cell[1]

            if (row !in 0 until GRID_SIZE ||
                col !in 0 until GRID_SIZE
            ) {
                return false
            }

            if (grid[row][col] != 0) {
                return false
            }
        }

        return true
    }

    private fun placeShape(
        shape: Array<IntArray>,
        startRow: Int,
        startCol: Int
    ) {
        for (cell in shape) {
            val row = startRow + cell[0]
            val col = startCol + cell[1]

            grid[row][col] = currentColor
        }
    }

    private fun clearCompletedLines(): Int {
        val rowsToClear = mutableListOf<Int>()
        val colsToClear = mutableListOf<Int>()

        for (r in 0 until GRID_SIZE) {
            if ((0 until GRID_SIZE).all { c ->
                    grid[r][c] != 0
                }) {
                rowsToClear.add(r)
            }
        }

        for (c in 0 until GRID_SIZE) {
            if ((0 until GRID_SIZE).all { r ->
                    grid[r][c] != 0
                }) {
                colsToClear.add(c)
            }
        }

        for (r in rowsToClear) {
            grid[r].fill(0)
        }

        for (c in colsToClear) {
            for (r in 0 until GRID_SIZE) {
                grid[r][c] = 0
            }
        }

        return rowsToClear.size + colsToClear.size
    }

    private fun hasAnyValidPlacement(): Boolean {
        for (shape in shapes) {
            for (r in 0 until GRID_SIZE) {
                for (c in 0 until GRID_SIZE) {
                    if (canPlaceShape(shape, r, c)) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun finishGame() {
        if (isFinished) return

        isFinished = true

        listener.onRequestSoundGameOver()
        listener.onGameOver(score)

        invalidate()
    }

    private fun finishWin() {
        if (isFinished) return

        isFinished = true

        listener.onRequestSoundSuccess()
        listener.onGameWon(score)

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.parseColor("#121212"))

        val topOffset = height * 0.16f
        val availableHeight = height - topOffset

        val cellSize = min(
            width.toFloat() / GRID_SIZE,
            availableHeight / GRID_SIZE
        )

        val boardWidth = cellSize * GRID_SIZE
        val boardLeft = (width - boardWidth) / 2f

        textPaint.textSize = width * 0.055f

        canvas.drawText(
            "BLOCK PUZZLE",
            width / 2f,
            width * 0.06f,
            textPaint
        )

        canvas.drawText(
            "Score: $score",
            width / 2f,
            width * 0.115f,
            textPaint
        )

        for (r in 0 until GRID_SIZE) {
            for (c in 0 until GRID_SIZE) {

                val left = boardLeft + c * cellSize
                val top = topOffset + r * cellSize
                val right = left + cellSize
                val bottom = top + cellSize

                cellPaint.color =
                    if (grid[r][c] == 0) {
                        Color.parseColor("#202020")
                    } else {
                        colors[grid[r][c]]
                    }

                canvas.drawRoundRect(
                    RectF(
                        left + CELL_PADDING,
                        top + CELL_PADDING,
                        right - CELL_PADDING,
                        bottom - CELL_PADDING
                    ),
                    cellSize * 0.12f,
                    cellSize * 0.12f,
                    cellPaint
                )

                canvas.drawRect(
                    left,
                    top,
                    right,
                    bottom,
                    borderPaint
                )
            }
        }

        if (isFinished) {

            textPaint.textSize = width * 0.065f

            val message =
                if (score >= WIN_SCORE) {
                    "YOU WIN!"
                } else {
                    "GAME OVER"
                }

            canvas.drawText(
                message,
                width / 2f,
                height * 0.94f,
                textPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (isFinished) {
            return true
        }

        if (event.action != MotionEvent.ACTION_DOWN) {
            return true
        }

        val topOffset = height * 0.16f
        val availableHeight = height - topOffset

        val cellSize = min(
            width.toFloat() / GRID_SIZE,
            availableHeight / GRID_SIZE
        )

        val boardWidth = cellSize * GRID_SIZE
        val boardLeft = (width - boardWidth) / 2f

        val col =
            ((event.x - boardLeft) / cellSize).toInt()

        val row =
            ((event.y - topOffset) / cellSize).toInt()

        if (row !in 0 until GRID_SIZE ||
            col !in 0 until GRID_SIZE
        ) {
            return true
        }

        if (!canPlaceShape(
                currentShape,
                row,
                col
            )
        ) {
            listener.onRequestSoundClick()
            listener.onRequestVibrate(25)

            return true
        }

        placeShape(
            currentShape,
            row,
            col
        )

        listener.onRequestSoundClick()
        listener.onRequestVibrate(30)

        val clearedLines = clearCompletedLines()

        score +=
            (currentShape.size * 10) +
            (clearedLines * 50)

        listener.onScoreChanged(score)

        if (score >= WIN_SCORE) {
            finishWin()
            return true
        }

        generateNextShape()

        invalidate()

        return true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isFinished = true
    }
}
