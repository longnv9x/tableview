package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.wiget.OnExcelPanelListener

class LeftRecyclerViewAdapter<L>(context: Context, list: List<L>, private val excelPanelListener: OnExcelPanelListener) : RecyclerViewAdapter<L>(context, list) {

    override fun getItemViewType(position: Int): Int {
        var viewType = super.getItemViewType(position)
        if (viewType == TYPE_NORMAL) {
            viewType = excelPanelListener.getLeftItemViewType(position)
        }
        return viewType
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return excelPanelListener.onCreateLeftViewHolder(parent, viewType)
    }

    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        excelPanelListener.onBindLeftViewHolder(holder, position)
        //use to adjust height
        holder.itemView.setTag(ExcelPanel.TAG_KEY, Pair(position, 0))
        excelPanelListener.onAfterBind(holder, position)
    }
}
