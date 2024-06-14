package com.marco.poccropimagewithgesture.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.marco.poccropimagewithgesture.ui.main.MainState
import com.marco.poccropimagewithgesture.utils.toPx

@Composable
fun DraggableRectangleCanvas(
    boxSizeWidth: Dp,
    boxSizeHeight: Dp,
    squareSize: Float,
    onRectanglePositionChanged: (Offset) -> Unit,
    density: Density,
    state: MainState,
    smallSquareSizeRatio: Float, // 0f to 1f
    style: DrawStyle = Fill,
) {
    // Calculate the initial position of the rectangle to center it
    val centerX = boxSizeWidth.toPx(density).minus(squareSize).div(2)
    val centerY = boxSizeHeight.toPx(density).minus(squareSize).div(2)

    // Set the initial position of the rectangle if not defined in the state
    if (state.offset == null) {
        state.offset = Offset(centerX, centerY)
    }

    // Remember the current offset and square size
    var offset by remember { mutableStateOf(state.offset ?: Offset(centerX, centerY)) }
    var currentSquareSize by remember { mutableFloatStateOf(squareSize) }

    // Update the square size dynamically and recenter the rectangle
    LaunchedEffect(squareSize) {
        currentSquareSize = squareSize
        offset = Offset(
            x = boxSizeWidth.toPx(density).minus(currentSquareSize).div(2),
            y = boxSizeHeight.toPx(density).minus(currentSquareSize).div(2)
        )
        // Callback to notify the new position
        onRectanglePositionChanged(offset)
    }

    Canvas(
        modifier = Modifier
            .size(width = boxSizeWidth, height = boxSizeHeight)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Calculate the new position of the rectangle with constraints based on current square size
                    val newOffset = offset + dragAmount
                    offset = Offset(
                        x = newOffset.x.coerceIn(0f, size.width - currentSquareSize),
                        y = newOffset.y.coerceIn(0f, size.height - currentSquareSize)
                    )
                    // Callback to notify the new position
                    onRectanglePositionChanged(offset)
                }
            }
    ) {
        // Draw the main rectangle at the current offset with the current square size
        drawRect(
            color = Color.Red,
            topLeft = offset,
            size = Size(currentSquareSize, currentSquareSize),
            style = style
        )

        // Calculate the size of the small squares based on the ratio
        val smallSquareSize = currentSquareSize * smallSquareSizeRatio
        val numSquares = (currentSquareSize / smallSquareSize).toInt()

        // Adjust smallSquareSize to fit perfectly within the main rectangle
        val adjustedSquareSize = currentSquareSize / numSquares

        // Draw the checkerboard pattern inside the main rectangle
        for (i in 0 until numSquares) {
            for (j in 0 until numSquares) {
                val topLeft = Offset(
                    x = offset.x + i * adjustedSquareSize,
                    y = offset.y + j * adjustedSquareSize
                )
                drawRect(
                    color = Color.Red,
                    topLeft = topLeft,
                    size = Size(adjustedSquareSize, adjustedSquareSize),
                    style = Stroke(width = 2f) // Stroke style to draw only the border
                )
            }
        }
    }
}