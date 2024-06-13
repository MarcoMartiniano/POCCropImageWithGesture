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

fun rescaleBitmap(
    originalBitmap: Bitmap,
    widthLimit: Int,
    heightLimit: Int,
): Bitmap {
    // originalWidth and originalHeight of the bitmap
    val originalWidth = originalBitmap.width
    val originalHeight = originalBitmap.height

    // Calculate the aspect ratio
    val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

    val (newWidth, newHeight) = if (originalWidth > widthLimit || originalHeight > heightLimit) {
        // Image exceeds limits, calculate new dimensions
        if (originalWidth.toFloat() / widthLimit > originalHeight.toFloat() / heightLimit) {
            // Width limit hit first
            widthLimit to (widthLimit / aspectRatio).toInt()
        } else {
            // Height limit hit first
            (heightLimit * aspectRatio).toInt() to heightLimit
        }
    } else {
        // Image is smaller than both limits, scale up to fit within limits
        if (widthLimit.toFloat() / originalWidth > heightLimit.toFloat() / originalHeight) {
            // Scale by height
            (heightLimit * aspectRatio).toInt() to heightLimit
        } else {
            // Scale by width
            widthLimit to (widthLimit / aspectRatio).toInt()
        }
    }
    return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
}