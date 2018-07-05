package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.example.omi.tableviewcustomer.Utils
import com.example.omi.tableviewcustomer.wiget.OnExcelPanelListener

abstract class BaseExcelPanelAdapter<T, L, M>(private val mContext: Context) : OnExcelPanelListener {
    companion object {
        const val LOADING_VIEW_WIDTH = 30
    }

    private var leftCellWidth: Int = 0
    private var topCellHeight: Int = 0
    private var amountAxisY = 0
    var topRecyclerViewAdapter: RecyclerViewAdapter<T>? = null
    var leftRecyclerViewAdapter: RecyclerViewAdapter<L>? = null
    var mRecyclerViewAdapter: RecyclerViewAdapter<M>? = null
    private var leftTopView: View? = null
    private var excelPanel: ExcelPanel? = null
    private var onScrollListener: RecyclerView.OnScrollListener? = null
    private var topData: List<T> = arrayListOf()
    private var leftData: List<L> = arrayListOf()
    private var majorData: List<List<M>> = arrayListOf()

    init {
        initRecyclerViewAdapter()
    }

    private fun initRecyclerViewAdapter() {
        topRecyclerViewAdapter = TopRecyclerViewAdapter(mContext, topData, this)
        leftRecyclerViewAdapter = LeftRecyclerViewAdapter(mContext, leftData, this)
        mRecyclerViewAdapter = MajorRecyclerViewAdapter(mContext, majorData, this) as RecyclerViewAdapter<M>
    }

    fun setTopData(topData: List<T>) {
        this.topData = topData
        topRecyclerViewAdapter!!.setData(topData)
    }

    fun setLeftData(leftData: List<L>) {
        this.leftData = leftData
        leftRecyclerViewAdapter!!.setData(leftData)
    }

    fun setMajorData(majorData: List<List<M>>) {
        this.majorData = majorData
        mRecyclerViewAdapter!!.setData(majorData as List<M>)
    }

    fun setAllData(leftData: ArrayList<L>, topData: ArrayList<T>, majorData: ArrayList<ArrayList<M>>) {
        setLeftData(leftData)
        setTopData(topData)
        setMajorData(majorData)
        excelPanel!!.scrollBy(0)
        excelPanel!!.fastScrollVerticalLeft()
        if (!Utils.isEmpty(leftData) && !Utils.isEmpty(topData) && excelPanel != null
                && !Utils.isEmpty(majorData) && leftTopView == null) {
            leftTopView = onCreateTopLeftView()
            excelPanel!!.addView(leftTopView, FrameLayout.LayoutParams(leftCellWidth, topCellHeight))
        } else if (leftTopView != null) {
            if (Utils.isEmpty(leftData)) {
                leftTopView!!.visibility = View.GONE
            } else {
                leftTopView!!.visibility = View.VISIBLE
            }
        }
    }

    fun setLeftCellWidth(leftCellWidth: Int) {
        this.leftCellWidth = leftCellWidth
    }

    fun setTopCellHeight(topCellHeight: Int) {
        this.topCellHeight = topCellHeight
    }

    fun setOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        this.onScrollListener = onScrollListener
        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter is MajorRecyclerViewAdapter<*>) {
            (mRecyclerViewAdapter as MajorRecyclerViewAdapter<*>).setOnScrollListener(onScrollListener)
        }
    }

    fun getTopItem(position: Int): T? {
        return if (Utils.isEmpty(topData) || position < 0 || position >= topData.size) {
            null
        } else topData[position]
    }

    fun getLeftItem(position: Int): L? {
        return if (Utils.isEmpty(leftData) || position < 0 || position >= leftData.size) {
            null
        } else leftData[position]
    }

    fun getMajorItem(x: Int, y: Int): M? {
        return if (Utils.isEmpty(majorData) || x < 0 || x >= majorData.size || Utils
                        .isEmpty(majorData[x]) || y < 0 || y >= majorData[x].size) {
            null
        } else majorData[x][y]
    }

    fun setAmountAxisY(amountAxisY: Int) {
        this.amountAxisY = amountAxisY
        if (mRecyclerViewAdapter != null && mRecyclerViewAdapter is MajorRecyclerViewAdapter<*>) {
            (mRecyclerViewAdapter as MajorRecyclerViewAdapter<*>).setAmountAxisY(amountAxisY)
        }
    }

    fun setExcelPanel(excelPanel: ExcelPanel) {
        this.excelPanel = excelPanel
    }

    private fun createTopStaticView(): View {
        val topStaticView = View(mContext)
        val loadingWidth = Utils.dp2px(LOADING_VIEW_WIDTH, mContext)
        topStaticView.layoutParams = ViewGroup.LayoutParams(loadingWidth, topCellHeight)

        return topStaticView
    }

    private fun createMajorLoadingView(): View {
        val loadingWidth = Utils.dp2px(LOADING_VIEW_WIDTH, mContext)
        val loadingView = LinearLayout(mContext)
        loadingView.orientation = LinearLayout.VERTICAL
        loadingView.gravity = Gravity.CENTER
        val lpp = LinearLayout.LayoutParams(loadingWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        loadingView.layoutParams = lpp

        val progressBar = ProgressBar(mContext)
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams(
                Utils.dp2px(16, mContext), Utils.dp2px(16, mContext)))
        progressBar.layoutParams = lp

        loadingView.addView(progressBar, lp)

        return loadingView
    }

    fun enableHeader() {
        if (topRecyclerViewAdapter != null && mRecyclerViewAdapter != null && excelPanel != null &&
                (topRecyclerViewAdapter!!.headerViewsCount <= 0 || mRecyclerViewAdapter!!.headerViewsCount <= 0)) {
            topRecyclerViewAdapter?.setHeaderView(createTopStaticView())
            mRecyclerViewAdapter?.setHeaderView(createMajorLoadingView())
            excelPanel?.setHasHeader(true)
            excelPanel?.scrollBy(Utils.dp2px(LOADING_VIEW_WIDTH, mContext))
        }
    }

    fun enableFooter() {
        if (topRecyclerViewAdapter != null && mRecyclerViewAdapter != null && excelPanel != null &&
                (topRecyclerViewAdapter!!.footerViewsCount <= 0 || mRecyclerViewAdapter!!.footerViewsCount <= 0)) {
            topRecyclerViewAdapter?.setFooterView(createTopStaticView())
            mRecyclerViewAdapter?.setFooterView(createMajorLoadingView())
            excelPanel?.setHasFooter(true)
        }
    }

    fun disableHeader() {
        if (topRecyclerViewAdapter != null && mRecyclerViewAdapter != null && excelPanel != null &&
                (topRecyclerViewAdapter!!.headerViewsCount > 0 || mRecyclerViewAdapter!!.headerViewsCount > 0)) {
            topRecyclerViewAdapter!!.setHeaderView(null)
            mRecyclerViewAdapter!!.setHeaderView(null)
            excelPanel?.setHasHeader(false)
            excelPanel?.scrollBy(-Utils.dp2px(LOADING_VIEW_WIDTH, mContext))
        }
    }

    fun disableFooter() {
        if (topRecyclerViewAdapter != null && mRecyclerViewAdapter != null && excelPanel != null &&
                (topRecyclerViewAdapter!!.footerViewsCount > 0 || mRecyclerViewAdapter!!.footerViewsCount > 0)) {
            topRecyclerViewAdapter?.setFooterView(null)
            mRecyclerViewAdapter?.setFooterView(null)
            excelPanel?.setHasFooter(false)

        }
    }

    override fun getCellItemViewType(verticalPosition: Int, horizontalPosition: Int): Int {
        return RecyclerViewAdapter.TYPE_NORMAL
    }

    override fun getLeftItemViewType(position: Int): Int {
        return RecyclerViewAdapter.TYPE_NORMAL
    }

    override fun getTopItemViewType(position: Int): Int {
        return RecyclerViewAdapter.TYPE_NORMAL
    }

    fun notifyDataSetChanged() {
        topRecyclerViewAdapter?.notifyDataSetChanged()
        leftRecyclerViewAdapter?.notifyDataSetChanged()
        (mRecyclerViewAdapter as MajorRecyclerViewAdapter<*>).customNotifyDataSetChanged()
    }

    override fun onAfterBind(holder: RecyclerView.ViewHolder, position: Int) {
            excelPanel?.onAfterBind(holder, position)
    }

}