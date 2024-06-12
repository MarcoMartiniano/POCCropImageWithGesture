package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

sealed class MainViewAction {
    object Crop {
        data class Image(val croppedBitmap: Bitmap) : MainViewAction()
    }
    object Gesture{
        data class SetOffset(val offset: Offset): MainViewAction()
    }
}