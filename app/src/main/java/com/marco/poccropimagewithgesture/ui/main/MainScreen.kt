package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marco.poccropimagewithgesture.R
import com.marco.poccropimagewithgesture.ui.components.DraggableRectangleCanvas
import org.koin.androidx.compose.koinViewModel


@Composable
fun MainScreenFactory(modifier: Modifier) {
    val viewModel: MainViewModel = koinViewModel()
    MainScreen(viewModel = viewModel, modifier = modifier)
}

@Preview
@Composable
fun PreviewMainScreen() {
    MainScreen(viewModel = MainViewModel(), modifier = Modifier)
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier,
) {
    // Define a callback to send actions to the ViewModel
    val action: (MainViewAction) -> Unit = { viewModel.dispatchViewAction(it) }

    // Observe the ViewModel state
    val viewState by viewModel.state.collectAsState()

    // Define the size of the crop square (in pixels)
    val cropSquareSize = 500f

    // Variable to store the cropped image bitmap
//    var imageBitmapThatICropped by remember { mutableStateOf<Bitmap?>(null) }

    // Define the initial position of the draggable rectangle
    var rectPosition by remember { mutableStateOf(Offset(0f, 0f)) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Load the image bitmap
        val imageBitmap = BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.mipmap.darthvader
        )

        // Define the size of the box that will contain the image
        val boxSize = 300.dp

        // Get the screen density
        val density = LocalDensity.current

        // Convert the box size from Dp to pixels
        val boxSizePx = with(density) { boxSize.toPx().toInt() }

        // Resize the original bitmap to the size of the box
        val scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, boxSizePx, boxSizePx, true)

        Box(
            modifier = Modifier
                .size(boxSize)
                .background(Color.Yellow)
        ) {
            // Display the resized image
            Image(bitmap = scaledBitmap.asImageBitmap(), contentDescription = "")

            // Composable that allows dragging a rectangle
            DraggableRectangleCanvas(
                boxSize = boxSize,
                squareSize = cropSquareSize,
                onRectanglePositionChanged = { newPosition ->
                    rectPosition = newPosition
                    viewState.offset = newPosition
                    action(MainViewAction.Gesture.SetOffset(offset = newPosition))
                },
                density = density,
                state = viewState,
                imageSize = boxSize
            )
        }

        // Crop button
        Button(onClick = {
            // Crop the image based on the rectangle position and size
            val croppedBitmap = cropImage(
                imageBitmap = scaledBitmap,
                squareSize = cropSquareSize,
                state = viewState
            )
            // Set the cropped image bitmap
            action(MainViewAction.Crop.Image(croppedBitmap = croppedBitmap))
        }) {
            Text("Crop Image")
        }

        // Column to display the cropped image
        Column(
            modifier = Modifier
                .size(300.dp)
                .background(Color.Yellow)
        ) {
            // Display the cropped image if available
            viewState.croppedBitmap?.let { croppedBitmap ->
                Image(bitmap = croppedBitmap.asImageBitmap(), contentDescription = "")
            }
        }
    }
}

private fun cropImage(
    imageBitmap: Bitmap,
    squareSize: Float,
    state: MainState,
): Bitmap {
    // Calculate the Rect based on the offset and square size
    val rect = offsetToRect(state.offset ?: Offset(0f, 0f), squareSize)

    // Create a bitmap with the cropped region
    val croppedBitmap = Bitmap.createBitmap(
        rect.width(),
        rect.height(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(croppedBitmap)

    // Draw the cropped image on the canvas
    canvas.drawBitmap(
        imageBitmap, // Original image to be cropped
        rect, // Offset of the cropped image (position where you want to crop)
        Rect(0, 0, rect.width(), rect.height()), // The rectangle that will be drawn
        null
    )
    return croppedBitmap
}

fun offsetToRect(
    offset: Offset,
    squareSize: Float,
): Rect {
    // Convert the coordinates and square size to integers
    val left = offset.x.toInt()
    val right = (offset.x + squareSize).toInt()
    val top = offset.y.toInt()
    val bottom = (offset.y + squareSize).toInt()

    return Rect(left, top, right, bottom)
}