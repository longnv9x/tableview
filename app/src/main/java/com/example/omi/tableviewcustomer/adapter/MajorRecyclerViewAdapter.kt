package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.Utils
import com.example.omi.tableviewcustomer.wiget.OnExcelPanelListener
import java.util.*

class MajorRecyclerViewAdapter<M>(private val context: Context, list: List<List<M>>, private val excelPanelListener: OnExcelPanelListener) : RecyclerViewAdapter<List<M>>(context, list) {
    private var amountAxisY = 0
    private var list: MutableList<String>? = null//a virtual list
    private var adapterList: MutableList<RecyclerView.Adapter<*>> = arrayListOf()
    private var onScrollListener: RecyclerView.OnScrollListener? = null

    override fun setData(data: List<List<M>>?) {
        super.setData(if (data == null) null else data[0] as List<List<M>>)
        if (data != null) {
            if (list == null || list!!.size >= data.size) {//refresh or first time
                list = ArrayList()
            }
            for (i in list!!.size until data.size) {
                list!!.add("")
            }
        } else {
            list = null
        }
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val recyclerView = RecyclerView(context)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        return RecyclerViewViewHolder(recyclerView)
    }

    override fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is RecyclerViewViewHolder) {
            return
        }
        val contentRecyclerAdapter = ContentRecyclerAdapter<String>(context, position, excelPanelListener)
        adapterList.add(contentRecyclerAdapter)
        contentRecyclerAdapter.setData(list)
        holder.recyclerView.adapter = contentRecyclerAdapter

        holder.recyclerView.removeOnScrollListener(onScrollListener)
        holder.recyclerView.addOnScrollListener(onScrollListener)
        excelPanelListener.fastScrollVertical(amountAxisY, holder.recyclerView)
    }

    internal class RecyclerViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView as RecyclerView
    }

    fun setAmountAxisY(amountAxisY: Int) {
        this.amountAxisY = amountAxisY
    }

    fun customNotifyDataSetChanged() {
        if (!Utils.isEmpty(adapterList)) {
            for (adapter in adapterList) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        this.onScrollListener = onScrollListener
    }
}