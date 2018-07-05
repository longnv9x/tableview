package com.example.omi.tableviewcustomer.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.omi.tableviewcustomer.R

class LeftHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val roomNumberLabel: TextView
    val roomTypeLabel: TextView
    val root: View

    init {
        root = itemView.findViewById(R.id.root)
        roomNumberLabel = itemView.findViewById(R.id.room_number_label)
        roomTypeLabel = itemView.findViewById(R.id.room_type_label)
    }
}