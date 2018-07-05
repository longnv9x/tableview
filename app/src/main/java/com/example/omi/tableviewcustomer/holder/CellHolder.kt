package com.example.omi.tableviewcustomer.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.omi.tableviewcustomer.R

class CellHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val bookingName: TextView
    val channelName: TextView
    val cellContainer: LinearLayout

    init {
        bookingName = itemView.findViewById(R.id.booking_name)
        channelName = itemView.findViewById(R.id.channel_name)
        cellContainer = itemView.findViewById(R.id.pms_cell_container)
    }
}