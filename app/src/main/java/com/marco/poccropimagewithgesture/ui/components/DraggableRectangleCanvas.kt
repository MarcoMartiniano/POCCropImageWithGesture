package com.marco.poccropimagewithgesture.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.marco.poccropimagewithgesture.ui.main.MainState
import com.marco.poccropimagewithgesture.utils.toPx

@Composable
fun DraggableRectangleCanvas(
    boxSize: Dp,
    squareSize: Float,
    onRectanglePositionChanged: (Offset) -> Unit,
    density: Density,
    state: MainState,
    imageSize: Dp,
) {
    // Calculate the initial position of the rectangle to center it
    val center = imageSize.toPx(density).minus(squareSize).div(2)

    // Set the initial position of the rectangle if not defined in the state
    if (state.offset == null) {
        state.offset = Offset(center, center)
    }

    var offset by remember { mutableStateOf(state.offset ?: Offset(center, center)) }

    Canvas(
        modifier = Modifier
            .size(width = boxSize, height = boxSize)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    // Calculate the new position of the rectangle with constraints
                    val newOffset = offset + dragAmount
                    offset = Offset(
                        x = newOffset.x.coerceIn(0f, size.width - squareSize),
                        y = newOffset.y.coerceIn(0f, size.height - squareSize)
                    )
                    // Callback to notify the new position
                    onRectanglePositionChanged(offset)
                }
            }
    ) {
        // Draw the rectangle at the current offset
        drawRect(
            color = Color.Red,
            topLeft = offset,
            size = Size(squareSize, squareSize),
            style = Stroke(width = 2.dp.toPx())
        )
    }
}