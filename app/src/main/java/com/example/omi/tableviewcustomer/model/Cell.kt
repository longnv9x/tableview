package com.example.omi.tableviewcustomer.model

class Cell {

    var status: Int = 0// 0没信息 ，3表示预定，2表示入住，1表示离店
    var channelName: String? = null//渠道名称,
    var bookingName: String? = null//预定人姓名
}
