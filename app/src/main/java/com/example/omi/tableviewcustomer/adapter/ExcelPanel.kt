package com.example.omi.tableviewcustomer.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.example.omi.tableviewcustomer.R
import com.example.omi.tableviewcustomer.Utils
import java.util.*


open class ExcelPanel(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), ViewTreeObserver.OnGlobalLayoutListener {
    companion object {
        const val TAG_KEY = R.id.lib_excel_panel_tag_key
        const val DEFAULT_LENGTH = 56
        const val LOADING_VIEW_WIDTH = 30
    }

    var leftCellWidth: Int = 0
    var topCellHeight: Int = 0
    var normalCellWidth: Int = 0
    var loadingViewWidth: Int = 0
    var amountAxisX = 0
    var amountAxisY = 0
    var dividerHeight: Int = 0
    var hasHeader: Boolean = false
    var hasFooter: Boolean = false
    var dividerLineVisible: Boolean = false

    var dividerLine: View? = null
    var mRecyclerView: RecyclerView? = null
    var topRecyclerView: RecyclerView? = null
    var leftRecyclerView: RecyclerView? = null
    var excelPanelAdapter: BaseExcelPanelAdapter<*, *, *>? = null
    var indexHeight: MutableMap<Int, Int>? = null

    private var onLoadMoreListener: OnLoadMoreListener? = null

    interface OnLoadMoreListener {
        /**
         * when the loading icon appeared, this method may be called many times
         */
        fun onLoadMore()

        /**
         * when the loading icon appeared, this method may be called many times. The excelPanel will dislocation
         * when the data have been added, you must call [addHistorySize(int)][.addHistorySize].
         */
        fun onLoadHistory()
    }

    init {
        val a = getContext().theme.obtainStyledAttributes(attrs, R.styleable.ExcelPanel, 0, 0)
        try {
            leftCellWidth = a.getDimension(R.styleable.ExcelPanel_left_cell_width, Utils.dp2px(DEFAULT_LENGTH, context).toFloat()).toInt()
            topCellHeight = a.getDimension(R.styleable.ExcelPanel_top_cell_height, Utils.dp2px(DEFAULT_LENGTH, context).toFloat()).toInt()
            normalCellWidth = a.getDimension(R.styleable.ExcelPanel_normal_cell_width, Utils.dp2px(DEFAULT_LENGTH, context).toFloat()).toInt()
        } finally {
            a.recycle()
        }
        indexHeight = TreeMap()
        loadingViewWidth = Utils.dp2px(LOADING_VIEW_WIDTH, context)
        initWidget()
    }

    private fun initWidget() {

        //content's RecyclerView
        mRecyclerView = createMajorContent()
        addView(mRecyclerView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        val mlp = mRecyclerView!!.layoutParams as FrameLayout.LayoutParams
        mlp.leftMargin = leftCellWidth
        mlp.topMargin = topCellHeight
        mRecyclerView!!.layoutParams = mlp

        //top RecyclerView
        topRecyclerView = createTopHeader()
        addView(topRecyclerView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, topCellHeight))
        val tlp = topRecyclerView!!.layoutParams as FrameLayout.LayoutParams
        tlp.leftMargin = leftCellWidth
        topRecyclerView!!.layoutParams = tlp

        //left RecyclerView
        leftRecyclerView = createLeftHeader()
        addView(leftRecyclerView, FrameLayout.LayoutParams(leftCellWidth, FrameLayout.LayoutParams.WRAP_CONTENT))
        val llp = leftRecyclerView!!.layoutParams as FrameLayout.LayoutParams
        llp.topMargin = topCellHeight
        leftRecyclerView!!.layoutParams = llp

        dividerLine = createDividerToLeftHeader()
        addView(dividerLine, ViewGroup.LayoutParams(1, ViewGroup.LayoutParams.WRAP_CONTENT))
        val lineLp = dividerLine?.layoutParams as FrameLayout.LayoutParams
        lineLp.leftMargin = leftCellWidth
        dividerLine?.layoutParams = lineLp
        viewTreeObserver.addOnGlobalLayoutListener(this)

    }

    override fun onGlobalLayout() {
        if (dividerHeight == measuredHeight && measuredHeight != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        }
        val lineLp1 = dividerLine?.layoutParams as FrameLayout.LayoutParams
        lineLp1.height = measuredHeight
        dividerHeight = lineLp1.height
        dividerLine?.layoutParams = lineLp1
    }

    private fun createTopHeader(): RecyclerView {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = getTopLayoutManager()
        recyclerView.addOnScrollListener(contentScrollListener)
        return recyclerView
    }

    private fun createLeftHeader(): RecyclerView {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = getLeftLayoutManager()
        recyclerView.addOnScrollListener(leftScrollListener)
        return recyclerView
    }

    private fun createMajorContent(): RecyclerView {
        val recyclerView = ExcelMajorRecyclerView(context)
        recyclerView.layoutManager = getLayoutManager()
        recyclerView.addOnScrollListener(contentScrollListener)
        return recyclerView
    }

    private fun createDividerToLeftHeader(): View {
        val view = View(context)
        view.visibility = View.GONE
        view.setBackgroundColor(context.resources.getColor(android.R.color.background_dark))
        return view
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager {
        if (null == mRecyclerView || null == mRecyclerView!!.layoutManager) {
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            return layoutManager
        }
        return mRecyclerView!!.layoutManager
    }

    private fun getTopLayoutManager(): RecyclerView.LayoutManager {
        if (null == topRecyclerView || null == topRecyclerView!!.layoutManager) {
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            return layoutManager
        }
        return topRecyclerView!!.layoutManager
    }

    private fun getLeftLayoutManager(): RecyclerView.LayoutManager {
        if (null == leftRecyclerView || null == leftRecyclerView!!.layoutManager) {
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            return layoutManager
        }
        return leftRecyclerView!!.layoutManager
    }

    /**
     * horizontal listener
     */
    private val contentScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            amountAxisX += dx
            fastScrollTo(amountAxisX, mRecyclerView, loadingViewWidth, hasHeader)
            fastScrollTo(amountAxisX, topRecyclerView, loadingViewWidth, hasHeader)
            if (dx == 0 && dy == 0) {
                return
            }
            val manager = recyclerView!!.layoutManager as LinearLayoutManager
            val visibleItemCount = recyclerView.childCount
            val totalItemCount = manager.itemCount
            val firstVisibleItem = manager.findFirstVisibleItemPosition()
            if (totalItemCount - visibleItemCount <= firstVisibleItem && onLoadMoreListener != null && hasFooter) {
                onLoadMoreListener!!.onLoadMore()
            }
            if (amountAxisX < loadingViewWidth && onLoadMoreListener != null && hasHeader) {
                onLoadMoreListener!!.onLoadHistory()
            }
            if ((hasHeader && amountAxisX > loadingViewWidth || !hasHeader && amountAxisX > 0) && dividerLineVisible) {
                dividerLine?.visibility = View.VISIBLE
            } else {
                dividerLine?.visibility = View.GONE
            }
        }
    }

    /**
     * vertical listener
     */
    private val leftScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            Log.e("ExcelPanel","$recyclerView is null ")
            super.onScrolled(recyclerView, dx, dy)
            //if (dy == 0) {return;} can't do this if use reset(amountAxisY==0),excelPanel will dislocation
            amountAxisY += dy
            for (i in 0 until mRecyclerView!!.childCount) {
                if (mRecyclerView!!.getChildAt(i) is RecyclerView) {
                    val recyclerView1 = mRecyclerView!!.getChildAt(i) as RecyclerView
                    fastScrollVertical(amountAxisY, recyclerView1)
                }
            }
            fastScrollVertical(amountAxisY, leftRecyclerView!!)
            if (excelPanelAdapter != null) {
                excelPanelAdapter!!.setAmountAxisY(amountAxisY)
            }
        }
    }

    fun fastScrollVerticalLeft() {
        fastScrollVertical(amountAxisY, leftRecyclerView!!)
    }

    fun fastScrollVertical(amountAxis: Int, recyclerView: RecyclerView) {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        if (indexHeight == null) {
            indexHeight = TreeMap()
            //call this method the OnScrollListener's onScrolled will be called，but dx and dy always be zero.
            linearLayoutManager.scrollToPositionWithOffset(0, -amountAxis)
        } else {
            var total = 0
            var count = 0
            val iterator = indexHeight!!.keys.iterator()
            while (iterator.hasNext()) {
                val height = indexHeight!![iterator.next()]
                if (total + height!! >= amountAxis) {
                    break
                }
                total += height
                count++
            }
            linearLayoutManager.scrollToPositionWithOffset(count, -(amountAxis - total))
        }
    }

    fun fastScrollTo(amountAxis: Int, recyclerView: RecyclerView?, offset: Int, hasHeader: Boolean) {
        var amountAxis = amountAxis
        var position = 0
        val width = normalCellWidth
        if (amountAxis >= offset && hasHeader) {
            amountAxis -= offset
            position++
        }
        position += amountAxis / width
        amountAxis %= width
        val linearLayoutManager = recyclerView!!.layoutManager as LinearLayoutManager
        //call this method the OnScrollListener's onScrolled will be called，but dx and dy always be zero.
        linearLayoutManager.scrollToPositionWithOffset(position, -amountAxis)
    }

    fun setAdapter(excelPanelAdapter: BaseExcelPanelAdapter<*, *, *>) {
        this.excelPanelAdapter = excelPanelAdapter
        this.excelPanelAdapter!!.setLeftCellWidth(leftCellWidth)
        this.excelPanelAdapter!!.setTopCellHeight(topCellHeight)
        this.excelPanelAdapter!!.setOnScrollListener(leftScrollListener)
        this.excelPanelAdapter!!.setExcelPanel(this)
        distributeAdapter()
    }

    private fun distributeAdapter() {
        if (leftRecyclerView != null) {
            leftRecyclerView!!.adapter = excelPanelAdapter!!.leftRecyclerViewAdapter
        }
        if (topRecyclerView != null) {
            topRecyclerView!!.adapter = excelPanelAdapter!!.topRecyclerViewAdapter
        }
        if (mRecyclerView != null) {
            mRecyclerView!!.adapter = excelPanelAdapter!!.mRecyclerViewAdapter
        }
    }

    /**
     * @param dx horizontal distance to scroll
     */
    internal fun scrollBy(dx: Int) {
        contentScrollListener.onScrolled(mRecyclerView, dx, 0)
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    internal fun setHasHeader(hasHeader: Boolean) {
        this.hasHeader = hasHeader
    }

    internal fun setHasFooter(hasFooter: Boolean) {
        this.hasFooter = hasFooter
    }

    fun canChildScrollUp(): Boolean {
        return amountAxisY > 0
    }

    fun reset() {
        if (excelPanelAdapter != null) {
            excelPanelAdapter!!.disableFooter()
            excelPanelAdapter!!.disableHeader()
        }
        if (indexHeight == null) {
            indexHeight = TreeMap()
        }
        indexHeight!!.clear()
        amountAxisY = 0
        amountAxisX = 0
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    fun addHistorySize(size: Int) {
        if (size > 0) {
            contentScrollListener.onScrolled(topRecyclerView, normalCellWidth * size, 0)
        }
    }

    fun findFirstVisibleItemPosition(): Int {
        val position = -1
        if (mRecyclerView!!.layoutManager != null && excelPanelAdapter != null) {
            val mLinearLayoutManager = mRecyclerView!!.layoutManager as LinearLayoutManager
            val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()
            return if (hasHeader) {
                firstVisibleItem - 1
            } else firstVisibleItem
        }
        return position
    }

    fun enableDividerLine(visible: Boolean) {
        dividerLineVisible = visible
    }

    /**
     * use to adjust the height and width of the normal cell
     *
     * @param holder   cell's holder
     * @param position horizontal or vertical position
     */
    fun onAfterBind(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder?.itemView != null) {
            if (indexHeight == null) {
                indexHeight = TreeMap()
            }
            val layoutParams = holder.itemView.layoutParams
            indexHeight!![position] = layoutParams.height
        }
    }
}