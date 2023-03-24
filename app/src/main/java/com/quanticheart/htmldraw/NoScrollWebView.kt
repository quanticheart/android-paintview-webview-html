package com.quanticheart.htmldraw

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

//
// Created by Jonn Alves on 15/03/23.
//
class NoScrollWebView : WebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    public override fun overScrollBy(
        deltaX: Int, deltaY: Int, scrollX: Int, scrollY: Int,
        scrollRangeX: Int, scrollRangeY: Int, maxOverScrollX: Int,
        maxOverScrollY: Int, isTouchEvent: Boolean
    ): Boolean {
        return false
    }

    override fun scrollTo(x: Int, y: Int) {
        // Do nothing
    }

    override fun computeScroll() {
        // Do nothing
    }
}