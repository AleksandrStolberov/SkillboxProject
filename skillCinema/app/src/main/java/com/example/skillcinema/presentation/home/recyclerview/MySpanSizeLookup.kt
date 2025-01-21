package com.example.skillcinema.presentation.home.recyclerview

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager

class MySpanSizeLookup(
    private val spanPos: Int,
    private val spanCount1: Int,
    private val spanCount2: Int
) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int {
        return (if ((position + 1) % spanPos == 0) spanCount2 else spanCount1)
    }

}