package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun dispatchViewAction(viewAction: MainViewAction) {
        when (viewAction) {
            is MainViewAction.Crop.Image -> updateCroppedBitmap(croppedBitmap = viewAction.croppedBitmap)
            is MainViewAction.Gesture.SetOffset -> updateGestureOffset(offset = viewAction.offset)
            is MainViewAction.Crop.SquareSize -> updateCropSquareSize(cropSquareSize = viewAction.cropSquareSize)
        }
    }

    private fun updateCropSquareSize(cropSquareSize: Float) {
        _state.update {
            it.copy(
                cropSquareSize = cropSquareSize,
            )
        }
    }

    private fun updateCroppedBitmap(croppedBitmap: Bitmap) {
        _state.update {
            it.copy(
                croppedBitmap = croppedBitmap,
            )
        }
    }

    private fun updateGestureOffset(offset: Offset) {
        _state.update {
            it.copy(
                offset = offset,
            )
        }
    }
}