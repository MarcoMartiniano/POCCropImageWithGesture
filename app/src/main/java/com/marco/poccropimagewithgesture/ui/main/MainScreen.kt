package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marco.poccropimagewithgesture.R
import com.marco.poccropimagewithgesture.ui.components.DraggableRectangleCanvas
import com.marco.poccropimagewithgesture.utils.cropImage
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
                offset = viewState.offset
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