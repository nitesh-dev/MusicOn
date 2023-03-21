package com.flaxstudio.musicon.utils

import android.content.res.Resources
import android.util.TypedValue

object Utils {

}

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics).toInt()