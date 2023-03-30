package com.flaxstudio.musicon.utils

import android.content.res.Resources
import android.util.TypedValue

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics).toInt()

class Vector2{
    var x = 0f
    var y = 0f

    fun setValue(x: Float, y: Float){
        this.x = x
        this.y = y
    }
}

enum class PlayType{
    OneLoop,
    Shuffle,
    Linear
}

enum class AppTheme{
    PINK,
    SHERPA_BLUE,
    BLUE,
    RED

}
fun String.toAppTheme(): AppTheme {
    var theme = AppTheme.PINK
    if(this == AppTheme.RED.toString()) theme = AppTheme.RED
    else if(this == AppTheme.BLUE.toString()) theme = AppTheme.BLUE
    else if(this == AppTheme.SHERPA_BLUE.toString()) theme = AppTheme.SHERPA_BLUE
    return theme
}


fun ArrayList<String>.toString(separator: String): String {
    var tempString = ""
    for (string in this){
        tempString += string + separator
    }

    return tempString
}


