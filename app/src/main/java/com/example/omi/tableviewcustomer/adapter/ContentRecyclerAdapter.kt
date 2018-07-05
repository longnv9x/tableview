package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.wiget.OnExcelPanelListener

class ContentRecyclerAdapter<C>(context: Context, private val verticalPosition: Int,
                                private val excelPanelListener: OnExcelPanelListener) : RecyclerViewAdapter<C>(context) {

    override fun getItemViewType(position: Int): Int {
        var viewType = super.getItemViewType(position)
        if (viewType == TYPE_NORMAL) {
            viewType = excelPanelListener.getCellItemViewType(position, verticalPosition)
        }
        return viewType
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return excelPanelListener.onCreateCellViewHolder(parent, viewType)

    }

    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        excelPanelListener.onBindCellViewHolder(holder, position, verticalPosition)
    }
}