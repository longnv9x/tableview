package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.omi.tableviewcustomer.Utils
import java.util.ArrayList

/**
 * Created by gcq on 16/8/19.
 */
abstract class RecyclerViewAdapter<T> @JvmOverloads constructor(mContext: Context, list: List<T>? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_FOOTER = 1
        const val TYPE_NORMAL = 2
    }

    var mHeader: View? = null
    var mFooter: View? = null
    var mDatas: List<T>? = null
    var mInflater: LayoutInflater

    val headerViewsCount: Int
        get() = if (null == mHeader) 0 else 1

    val footerViewsCount: Int
        get() = if (null == mFooter) 0 else 1

    val realCount: Int
        get() = if (null == mDatas) 0 else mDatas!!.size

    init {
        mDatas = list
        if (list != null) {
            this.mDatas = ArrayList(list)
        }
        this.mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    open fun setData(data: List<T>?) {
        if (data == null) {
            this.mDatas = null
        } else {
            this.mDatas = ArrayList(data)
        }
        this.notifyDataSetChanged()
    }

    fun setHeaderView(headerView: View?) {
        if (null == headerView) {
            if (null != mHeader) {
                mHeader = null
                notifyItemRemoved(0)
            }
        } else {
            if (null != mHeader) {
                if (mHeader !== headerView) {
                    mHeader = headerView
                    notifyItemChanged(0)
                }
            } else {
                mHeader = headerView
                notifyItemInserted(0)
            }
        }
    }

    fun setFooterView(footerView: View?) {
        if (null == footerView) {
            if (null != mFooter) {
                mFooter = null
                notifyItemRemoved(itemCount)
            }
        } else {
            if (null != mFooter) {
                if (mFooter !== footerView) {
                    mFooter = footerView
                    notifyItemChanged(itemCount)
                }
            } else {
                mFooter = footerView
                notifyItemInserted(itemCount)
            }
        }
    }

    fun getItem(position: Int): T? {
        return if (Utils.isEmpty(mDatas) || position < 0 || position >= mDatas!!.size) {
            null
        } else this.mDatas!![position]
    }

    override fun getItemViewType(position: Int): Int {
        if (null != mHeader && position == 0) {
            return TYPE_HEADER
        }
        return if (null != mFooter && position == itemCount - 1) {
            TYPE_FOOTER
        } else TYPE_NORMAL
    }

    override fun getItemCount(): Int {
        var size = headerViewsCount
        size += footerViewsCount
        if (null != mDatas) {
            size += mDatas!!.size
        }
        return size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> HeaderFooterHolder(mHeader!!)
            TYPE_FOOTER -> HeaderFooterHolder(mFooter!!)
            else -> onCreateNormalViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == TYPE_NORMAL) {
            onBindNormalViewHolder(holder, position - headerViewsCount)
        }
    }


    abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    abstract fun onBindNormalViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    internal class HeaderFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
