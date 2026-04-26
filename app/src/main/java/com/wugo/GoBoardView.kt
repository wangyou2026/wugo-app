package com.wugo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.roundToInt

data class Stone(
    val x: Int,
    val y: Int,
    val color: StoneColor
)

enum class StoneColor {
    BLACK, WHITE
}

@Composable
fun GoBoard(
    boardSize: Int = 19,
    stones: Map<String, Stone>,
    onStonePlaced: (Int, Int, StoneColor) -> Unit,
    interactive: Boolean = true,
    solutionMode: Boolean = false
) {
    val currentStoneColor = remember { mutableStateOf(StoneColor.BLACK) }

    Canvas(
        modifier = Modifier
            .aspectRatio(1f)
            .pointerInput(interactive) {
                if (!interactive) return@pointerInput
                detectTapGestures { offset ->
                    val gridX = (offset.x / size.width * boardSize).roundToInt()
                    val gridY = (offset.y / size.height * boardSize).roundToInt()

                    if (gridX >= 0 && gridX < boardSize && gridY >= 0 && gridY < boardSize) {
                        onStonePlaced(gridX, gridY, currentStoneColor.value)
                        currentStoneColor.value =
                            if (currentStoneColor.value == StoneColor.BLACK) StoneColor.WHITE
                            else StoneColor.BLACK
                    }
                }
            }
    ) {
        drawBoard(boardSize, stones)
    }
}

private fun DrawScope.drawBoard(boardSize: Int, stones: Map<String, Stone>) {
    val padding = size.width / boardSize * 0.5f
    val cellSize = (size.width - 2 * padding) / (boardSize - 1)

    // 绘制棋盘背景（木纹色）
    drawRect(color = Color(0xFF5D4037))

    // 绘制棋盘线
    for (i in 0 until boardSize) {
        val x = padding + i * cellSize
        val y = padding + i * cellSize

        drawLine(
            color = Color.Black,
            start = Offset(x, padding),
            end = Offset(x, size.height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = Offset(padding, y),
            end = Offset(size.width - padding, y),
            strokeWidth = 2f
        )
    }

    // 绘制星位点（仅 19 路）
    if (boardSize == 19) {
        val starPositions = listOf(3, 9, 15)
        for (sx in starPositions) {
            for (sy in starPositions) {
                drawCircle(
                    color = Color.Black,
                    radius = 6f,
                    center = Offset(padding + sx * cellSize, padding + sy * cellSize)
                )
            }
        }
    }

    // 绘制棋子
    stones.forEach { (_, stone) ->
        val x = padding + stone.x * cellSize
        val y = padding + stone.y * cellSize

        val stoneColor = if (stone.color == StoneColor.BLACK) Color.Black else Color.White

        drawCircle(
            color = stoneColor,
            radius = cellSize * 0.45f,
            center = Offset(x, y)
        )

        // 白棋添加边框
        if (stone.color == StoneColor.WHITE) {
            drawCircle(
                color = Color(0xFF5D4037),
                radius = cellSize * 0.45f,
                center = Offset(x, y),
                style = Stroke(width = 2f)
            )
        }
        // 黑棋添加高光
        else {
            drawCircle(
                color = Color(0x33FFFFFF),
                radius = cellSize * 0.15f,
                center = Offset(x - cellSize * 0.1f, y - cellSize * 0.1f)
            )
        }
    }
}

fun boardCoordinatesToString(x: Int, y: Int): String {
    return "${('A' + x)}${y + 1}"
}
