package com.marco.poccropimagewithgesture.utils

import kotlin.math.round

fun Float.roundToFirstDecimalPlace(): Float {
    return round(this * 10) / 10f
}