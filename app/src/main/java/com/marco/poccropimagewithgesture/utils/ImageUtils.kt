package com.marco.poccropimagewithgesture.utils

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.compose.ui.geometry.Offset

// Function to crop an image bitmap based on an offset and square size
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

// Function to convert an offset and square size to a Rect
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

// Function to rescale a bitmap to fit within specified width and height limits, or to increase to minimum limits while keeping proportions
fun rescaleBitmap(
    originalBitmap: Bitmap,
    widthLimit: Int,
    heightLimit: Int,
    minimumLimit: Int,
): Bitmap {
    val originalWidth = originalBitmap.width
    val originalHeight = originalBitmap.height

    // Calculate the aspect ratio
    val aspectRatioWidth = originalWidth.toFloat() / originalHeight.toFloat()
    val aspectRatioHeight = originalHeight.toFloat() / originalWidth.toFloat()

    val rescaleType = RescaleType.analiseRescaleType(
        originalWidth = originalWidth,
        originalHeight = originalHeight,
        widthLimit = widthLimit,
        heightLimit = heightLimit,
        minimumLimit = minimumLimit
    )
    var newWidth = 0
    var newHeight = 0

    // Determine new dimensions based on the rescale type
    when (rescaleType) {
        // No rescaling needed, keep original dimensions
        RescaleType.ORIGINAL -> {
            newWidth = originalWidth
            newHeight = originalHeight
        }
        // Increase to the minimum limit for width, adjusting height proportionally
        RescaleType.RESIZE_TO_WIDTH_CROP_SQUARE -> {
            newWidth = minimumLimit
            newHeight = (minimumLimit * aspectRatioHeight).toInt()
        }
        // Increase to the minimum limit for height, adjusting width proportionally
        RescaleType.RESIZE_TO_HEIGHT_CROP_SQUARE -> {
            newHeight = minimumLimit
            newWidth = (minimumLimit * aspectRatioWidth).toInt()
        }
        // Resize to fit within maximum limits while maintaining aspect ratio
        RescaleType.RESIZE_TO_CONTAINER_SIZE -> {
            val (newWidth1, newHeight1) = resizeRectangleToFit(
                originalWidth,
                originalHeight,
                widthLimit,
                heightLimit
            )
            newHeight = newHeight1
            newWidth = newWidth1
        }
    }
    return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
}

// Function to resize a rectangle to fit within a bounding rectangle while maintaining aspect ratio
fun resizeRectangleToFit(
    originalWidth: Int,
    originalHeight: Int,
    boundingWidth: Int,
    boundingHeight: Int,
): Pair<Int, Int> {
    // Calculate the scaling factors for width and height
    val widthRatio = boundingWidth.toFloat() / originalWidth
    val heightRatio = boundingHeight.toFloat() / originalHeight
    // Choose the smaller scaling factor to ensure the image fits within the bounding box
    val scaleFactor = minOf(widthRatio, heightRatio)

    // Calculate new dimensions based on the scaling factor
    val newWidth = (originalWidth * scaleFactor).toInt()
    val newHeight = (originalHeight * scaleFactor).toInt()

    return newWidth to newHeight
}

// Enum class for different rescale types
enum class RescaleType {
    RESIZE_TO_CONTAINER_SIZE,
    RESIZE_TO_WIDTH_CROP_SQUARE,
    RESIZE_TO_HEIGHT_CROP_SQUARE,
    ORIGINAL;

    companion object {
        // Function to analyze and determine the appropriate rescale type
        fun analiseRescaleType(
            originalWidth: Int,
            originalHeight: Int,
            widthLimit: Int,
            heightLimit: Int,
            minimumLimit: Int,
        ): RescaleType {
            // Calculate the scaling factors for width and height
            val widthRatio = widthLimit.toFloat() / originalWidth
            val heightRatio = heightLimit.toFloat() / originalHeight
            // Choose the smaller scaling factor to ensure the image fits within the bounding box
            val scaleFactor = minOf(widthRatio, heightRatio)

            // Calculate new dimensions based on the scaling factor
            val newWidth = (originalWidth * scaleFactor).toInt()
            val newHeight = (originalHeight * scaleFactor).toInt()

            return when {
                // Make the Crop square the minimum value to avoid crashes when trying to gesture outside the box
                minimumLimit >= newWidth || minimumLimit >= newHeight -> {
                    if (originalWidth < originalHeight) {
                        RESIZE_TO_WIDTH_CROP_SQUARE
                    } else {
                        RESIZE_TO_HEIGHT_CROP_SQUARE
                    }
                }
                // If original dimensions match the limits, no rescaling needed
                originalWidth == widthLimit && originalHeight == heightLimit -> ORIGINAL
                // Resize to the size of the container
                else -> RESIZE_TO_CONTAINER_SIZE
            }
        }
    }
}