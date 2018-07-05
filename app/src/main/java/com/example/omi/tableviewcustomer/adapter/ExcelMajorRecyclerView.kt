package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import com.example.omi.tableviewcustomer.Utils

class ExcelMajorRecyclerView(context: Context) : RecyclerView(context) {
    companion object {
        const val CONST_FIVE = 5
    }
    private var lastX: Int = 0
    private var lastY: Int = 0

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val x = ev.x.toInt()
        val y = ev.y.toInt()

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
                onTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> intercept = Math.abs(x - lastX) - Utils.dp2px(CONST_FIVE, context) > Math.abs(y - lastY)
            MotionEvent.ACTION_UP -> intercept = false
            else -> intercept = super.onInterceptTouchEvent(ev)
        }
        lastX = x
        lastY = y
        return intercept
    }

}
