package com.marco.poccropimagewithgesture.ui.main

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset

data class MainState(
    var offset: Offset? = null,
    val croppedBitmap: Bitmap? = null,
    val cropSquareSize: Float = 400f,
)