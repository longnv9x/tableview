package com.example.omi.tableviewcustomer.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.omi.tableviewcustomer.R

class TopHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val roomDate: TextView
    val roomWeek: TextView
    val availableRoomCount: TextView

    init {
        roomDate = itemView.findViewById(R.id.data_label)
        roomWeek = itemView.findViewById(R.id.week_label)
        availableRoomCount = itemView.findViewById(R.id.available_room_count)
    }
}