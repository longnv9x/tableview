package com.example.omi.tableviewcustomer

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.omi.tableviewcustomer.adapter.ExcelPanel
import com.example.omi.tableviewcustomer.model.Cell
import com.example.omi.tableviewcustomer.model.ColTitle
import com.example.omi.tableviewcustomer.model.RowTitle
import com.example.omi.tableviewcustomer.view.CustomAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ExcelFragment : Fragment(), ExcelPanel.OnLoadMoreListener, CustomAdapter.Interactor {
    override fun onClick(view: View) {
        Toast.makeText(context, "onclick", Toast.LENGTH_LONG)
        val cell = view.tag as Cell
        when {
            cell.status === 0 -> Toast.makeText(activity, "空房", Toast.LENGTH_SHORT).show()
            cell.status === 1 -> Toast.makeText(activity, "已离店，离店人：" + cell.bookingName, Toast.LENGTH_SHORT).show()
            cell.status === 2 -> Toast.makeText(activity, "入住中，离店人：" + cell.bookingName, Toast.LENGTH_SHORT).show()
            cell.status === 3 -> Toast.makeText(activity, "预定中，离店人：" + cell.bookingName, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFastScrollVertical(amountAxisY: Int, recyclerView: RecyclerView) {
        excelPanel.fastScrollVertical(amountAxisY, recyclerView)
    }

    companion object {
        const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
        const val WEEK_FORMAT_PATTERN = "EEEE"
        val CHANNEL = arrayOf("途牛", "携程", "艺龙", "去哪儿", "其他")
        val NAME = arrayOf("刘亦菲", "迪丽热巴", "柳岩", "范冰冰", "唐嫣", "杨幂", "刘诗诗")
        const val ONE_DAY = (24 * 3600 * 1000).toLong()
        const val PAGE_SIZE = 5
        const val ROW_SIZE = 20
    }

    lateinit var excelPanel: ExcelPanel
    lateinit var progress: ProgressBar
    lateinit var adapter: CustomAdapter
    private var rowTitles: ArrayList<RowTitle>? = null
    private var colTitles: ArrayList<ColTitle>? = null
    private var cells: ArrayList<ArrayList<Cell>>? = null
    private var dateFormatPattern: SimpleDateFormat? = null
    private var weekFormatPattern: SimpleDateFormat? = null
    private var isLoading: Boolean = false
    private var historyStartTime: Long = 0
    private var moreStartTime: Long = 0


    private val loadDataHandler = Handler { msg ->
        isLoading = false
        val startTime = msg.obj as Long
        val rowTitles1 = genRowData(startTime)
        val cells1 = genCellData()
        if (msg.arg1 == 1) {//history
            historyStartTime -= ONE_DAY * PAGE_SIZE
            rowTitles!!.addAll(0, rowTitles1)
            for (i in cells1.indices) {
                cells!![i].addAll(0, cells1[i])
            }

            //加载了数据之后偏移到上一个位置去

            excelPanel.addHistorySize(PAGE_SIZE)
        } else {
            moreStartTime += ONE_DAY * PAGE_SIZE
            rowTitles!!.addAll(rowTitles1)
            for (i in cells1.indices) {
                cells!![i].addAll(cells1[i])
            }
        }
        if (colTitles!!.size == 0) {
            colTitles!!.addAll(genColData())
        }
        progress.visibility = View.GONE
        adapter.setAllData(colTitles!!, rowTitles!!, cells!!)
        adapter.enableFooter()
        adapter.enableHeader()
        return@Handler true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.excel_fragment, container, false)
        progress = root.findViewById(R.id.progress)
        excelPanel = root.findViewById(R.id.content_container)
        adapter = CustomAdapter(activity, this)
        excelPanel.setAdapter(adapter)
        excelPanel.setOnLoadMoreListener(this)
        initData()
        return root
    }

    override fun onLoadMore() {
        if (!isLoading) {
            loadData(moreStartTime, false)
        }
    }

    override fun onLoadHistory() {
        if (!isLoading) {
            loadData(historyStartTime, true)
        }
    }

    private fun initData() {
        moreStartTime = Calendar.getInstance().timeInMillis
        historyStartTime = moreStartTime - ONE_DAY * PAGE_SIZE
        dateFormatPattern = SimpleDateFormat(DATE_FORMAT_PATTERN)
        weekFormatPattern = SimpleDateFormat(WEEK_FORMAT_PATTERN)
        rowTitles = arrayListOf()
        colTitles = arrayListOf()
        cells = arrayListOf()
        for (i in 0 until ROW_SIZE) {
            cells?.add(arrayListOf())
        }
        loadData(moreStartTime, false)
    }

    private fun loadData(startTime: Long, history: Boolean) {
        //模拟网络加载
        isLoading = true
        val message = Message()
        message.arg1 = if (history) 1 else 2
        message.obj = startTime
        loadDataHandler.sendMessageDelayed(message, 1000)
    }

    //====================================模拟生成数据==========================================
    private fun genRowData(startTime: Long): List<RowTitle> {
        val rowTitles = ArrayList<RowTitle>()
        val random = Random()
        for (i in 0 until PAGE_SIZE) {
            val rowTitle = RowTitle()
            rowTitle.availableRoomCount = random.nextInt(10) + 10
            rowTitle.dateString = dateFormatPattern!!.format(startTime + i * ONE_DAY)
            rowTitle.weekString = weekFormatPattern!!.format(startTime + i * ONE_DAY)
            rowTitles.add(rowTitle)
        }
        return rowTitles
    }

    private fun genColData(): List<ColTitle> {
        val colTitles = ArrayList<ColTitle>()
        for (i in 0 until ROW_SIZE) {
            val colTitle = ColTitle()
            if (i < 10) {
                colTitle.roomNumber = "10$i"
            } else {
                colTitle.roomNumber = "20" + (i - 10)
            }
            if (i % 3 == 0) {
                colTitle.roomTypeName = "大床房$i"
            } else if (i % 3 == 1) {
                colTitle.roomTypeName = "星空房$i"
            } else {
                colTitle.roomTypeName = "海景房$i"
            }
            colTitles.add(colTitle)
        }
        return colTitles
    }

    private fun genCellData(): List<List<Cell>> {
        val cells = ArrayList<List<Cell>>()
        for (i in 0 until ROW_SIZE) {
            val cellList = ArrayList<Cell>()
            cells.add(cellList)
            for (j in 0 until PAGE_SIZE) {
                val cell = Cell()
                val random = Random()
                val number = random.nextInt(6)
                if (number == 1 || number == 2 || number == 3) {
                    cell.status = number
                    cell.channelName = CHANNEL[random.nextInt(CHANNEL.size)]
                    cell.bookingName = NAME[random.nextInt(NAME.size)]
                } else {
                    cell.status = 0
                }
                cellList.add(cell)
            }
        }
        return cells
    }
}