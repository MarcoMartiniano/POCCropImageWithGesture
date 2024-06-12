package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.marco.poccropimagewithgesture.ui.main.MainState
import com.marco.poccropimagewithgesture.ui.main.MainViewAction
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