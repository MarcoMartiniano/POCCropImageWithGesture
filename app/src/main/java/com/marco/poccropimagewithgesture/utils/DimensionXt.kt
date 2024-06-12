package com.marco.poccropimagewithgesture.utils

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Dp.toPx(density: Density): Float {
    return this.value * density.density
}