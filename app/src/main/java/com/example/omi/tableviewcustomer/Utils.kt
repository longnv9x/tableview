package com.example.omi.tableviewcustomer

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.omi.tableviewcustomer.adapter.RecyclerViewAdapter
import java.util.*

object Utils {

    fun dp2px(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    fun <T> asList(vararg arr: T): List<T>? {
        return if (arr == null) null else ArrayList(Arrays.asList(*arr))
    }

    fun <T> isEmpty(list: List<T>?): Boolean {
        return list == null || list.isEmpty()
    }

    fun <T> inArray(t: T?, list: List<T>): Boolean {
        return t != null && !isEmpty(list) && list.contains(t)
    }

    fun <T> addAll(list: MutableList<T>, vararg ts: T) {
        val newList = Arrays.asList(*ts)
        list.addAll(newList)
    }

    fun <T> size(list: List<T>): Int {
        var size = 0
        if (!isEmpty(list)) {
            size = list.size
        }

        return size
    }
}