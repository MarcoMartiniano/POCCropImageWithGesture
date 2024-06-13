package com.marco.poccropimagewithgesture.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}

fun Density.intToDp(px: Int): Dp {
    return (px / density).dp
}