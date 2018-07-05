package com.example.omi.tableviewcustomer.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.R
import com.example.omi.tableviewcustomer.adapter.BaseExcelPanelAdapter
import com.example.omi.tableviewcustomer.holder.CellHolder
import com.example.omi.tableviewcustomer.holder.LeftHolder
import com.example.omi.tableviewcustomer.holder.TopHolder
import com.example.omi.tableviewcustomer.model.Cell
import com.example.omi.tableviewcustomer.model.ColTitle
import com.example.omi.tableviewcustomer.model.RowTitle

class CustomAdapter(private val context: Context, private val interactor: Interactor) : BaseExcelPanelAdapter<RowTitle, ColTitle, Cell>(context) {
    override fun fastScrollVertical(amountAxisY: Int, recyclerView: RecyclerView) {
        interactor.onFastScrollVertical(amountAxisY, recyclerView)
    }

    //=========================================content's cell===========================================
    override fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.room_status_normal_cell, parent, false)
        return CellHolder(layout)
    }

    override fun onBindCellViewHolder(holder: RecyclerView.ViewHolder, verticalPosition: Int, horizontalPosition: Int) {
        val cell = getMajorItem(verticalPosition, horizontalPosition)
        if (holder !is CellHolder || cell == null) {
            return
        }
        holder.cellContainer.tag = cell
        holder.cellContainer.setOnClickListener {
            interactor.onClick(it)
        }
        if (cell.status === 0) {
            holder.bookingName.text = ""
            holder.channelName.text = ""
            holder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.bookingName.text = cell.bookingName
            holder.channelName.text = cell.channelName
            when {
                cell.status === 1 -> holder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.left))
                cell.status === 2 -> holder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.staying))
                else -> holder.cellContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.booking))
            }
        }
    }

    //=========================================top cell===========================================
    override fun onCreateTopViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.room_status_top_header_item, parent, false)
        return TopHolder(layout)
    }

    override fun onBindTopViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rowTitle = getTopItem(position)
        if (holder !is TopHolder || rowTitle == null) {
            return
        }
        holder.roomWeek.text = rowTitle.weekString
        holder.roomDate.text = rowTitle.dateString
        holder.availableRoomCount.text = "剩余" + rowTitle.availableRoomCount + "间"
    }

    //=========================================left cell===========================================
    override fun onCreateLeftViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.room_status_left_header_item, parent, false)
        return LeftHolder(layout)
    }

    override fun onBindLeftViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val colTitle = getLeftItem(position)
        if (holder !is LeftHolder || colTitle == null) {
            return
        }
        holder.roomNumberLabel.text = colTitle.roomNumber
        holder.roomTypeLabel.text = colTitle.roomTypeName
        val lp = holder.root.layoutParams
        holder.root.layoutParams = lp
    }

    //=========================================left-top cell===========================================
    override fun onCreateTopLeftView(): View {
        return LayoutInflater.from(context).inflate(R.layout.room_status_normal_cell, null)
    }

    interface Interactor {
        fun onClick(view: View)
        fun onFastScrollVertical(amountAxisY: Int, recyclerView: RecyclerView)
    }
}