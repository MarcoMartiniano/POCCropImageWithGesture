package com.marco.poccropimagewithgesture.ui.main

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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marco.poccropimagewithgesture.R
import com.marco.poccropimagewithgesture.ui.components.DraggableRectangleCanvas
import com.marco.poccropimagewithgesture.utils.cropImage
import com.marco.poccropimagewithgesture.utils.intToDp
import com.marco.poccropimagewithgesture.utils.rescaleBitmap
import com.marco.poccropimagewithgesture.utils.toPx
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
    val cropSquareSize = 400f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Load the image bitmap
        val imageBitmap = BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.mipmap.darthvader_rectangule_vertical
        )
        // Get the screen density
        val density = LocalDensity.current

        // Define the heightLimit and widthLimit of the image
        val heightLimit = 300.dp.toPx(density).toInt()
        val widthLimit = screenWidthPercentage(0.7f)

        // Resize the original bitmap to the limit size
        val scaledBitmap = rescaleBitmap(
            originalBitmap = imageBitmap,
            widthLimit = widthLimit,
            heightLimit = heightLimit,
            minimumLimit = cropSquareSize.toInt()
        )

        // Define the size of the box equals the size of scaledBitmap
        val boxSizeX = scaledBitmap.width
        val boxSizeY = scaledBitmap.height
        val boxSizeXdp = density.intToDp(boxSizeX)
        val boxSizeYdp = density.intToDp(boxSizeY)

        Box(
            modifier = Modifier
                .size(width = boxSizeXdp, height = boxSizeYdp)
                .background(Color.Yellow)
        ) {
            // Display the resized image
            Image(bitmap = scaledBitmap.asImageBitmap(), contentDescription = "")

            // Composable that allows dragging a rectangle
            DraggableRectangleCanvas(
                boxSizeWidth = boxSizeXdp,
                boxSizeHeight = boxSizeYdp,
                squareSize = cropSquareSize,
                onRectanglePositionChanged = { newPosition ->
                    viewState.offset = newPosition
                    action(MainViewAction.Gesture.SetOffset(offset = newPosition))
                },
                density = density,
                state = viewState,
                Stroke(width = 2.dp.toPx(density = density))
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

@Composable
fun screenWidthPercentage(percentage: Float): Int {
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp * percentage * LocalDensity.current.density
    return screenWidthPx.toInt()
}