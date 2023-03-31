package com.flaxstudio.musicon.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView


class WrapContentRecyclerView(context: Context, attrs: AttributeSet) :
    RecyclerView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var height = 0
        if(adapter != null && adapter!!.itemCount > 0){
            height = adapter!!.itemCount * getChildAt(0).measuredHeight
        }

        setMeasuredDimension(measuredWidth, height)
    }

}