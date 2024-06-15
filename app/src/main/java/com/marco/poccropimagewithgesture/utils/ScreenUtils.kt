package com.marco.poccropimagewithgesture.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity

@Composable
fun screenWidthPercentage(percentage: Float): Int {
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp * percentage * LocalDensity.current.density
    return screenWidthPx.toInt()
}