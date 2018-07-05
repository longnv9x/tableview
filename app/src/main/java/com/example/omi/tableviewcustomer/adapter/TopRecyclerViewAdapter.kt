package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.wiget.OnExcelPanelListener

class TopRecyclerViewAdapter<T>(context: Context, list: List<T>, private val excelPanelListener: OnExcelPanelListener) : RecyclerViewAdapter<T>(context, list) {

    override fun getItemViewType(position: Int): Int {
        var viewType = super.getItemViewType(position)
        if (viewType == TYPE_NORMAL) {
            viewType = excelPanelListener.getTopItemViewType(position)
        }
        return viewType
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return excelPanelListener.onCreateTopViewHolder(parent, viewType)
    }

    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        excelPanelListener.onBindTopViewHolder(holder, position)
    }
}