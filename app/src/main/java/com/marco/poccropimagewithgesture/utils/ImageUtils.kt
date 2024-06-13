package com.marco.poccropimagewithgesture.utils

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset

fun cropImage(
    imageBitmap: Bitmap,
    squareSize: Float,
    offset: Offset?,
): Bitmap {
    // Calculate the Rect based on the offset and square size
    val rect = offsetToRect(offset ?: Offset(0f, 0f), squareSize)

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