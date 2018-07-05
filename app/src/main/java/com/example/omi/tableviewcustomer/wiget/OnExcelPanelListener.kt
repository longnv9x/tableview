package com.example.omi.tableviewcustomer.wiget

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

interface OnExcelPanelListener {

    /**
     * create normal cell's holder
     *
     * @param parent   parent
     * @param viewType viewType
     * @return ViewHolder holder
     */
    fun onCreateCellViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * bind normal cell data
     *
     * @param holder             holder
     * @param verticalPosition   verticalPosition, first dimension
     * @param horizontalPosition horizontalPosition, second dimension
     */
    fun onBindCellViewHolder(holder: RecyclerView.ViewHolder, verticalPosition: Int, horizontalPosition: Int)

    /**
     * create topHeader cell's holder
     *
     * @param parent   parent
     * @param viewType viewType
     * @return ViewHolder holder
     */
    fun onCreateTopViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * bind topHeader cell's data
     *
     * @param holder   ViewHolder
     * @param position position
     */
    fun onBindTopViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    /**
     * create leftHeader cell's holder
     *
     * @param parent   parent
     * @param viewType viewType
     * @return ViewHolder holder
     */
    fun onCreateLeftViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * bind leftHeader cell's data
     *
     * @param holder   ViewHolder
     * @param position position
     */
    fun onBindLeftViewHolder(holder: RecyclerView.ViewHolder, position: Int)


    /**
     * @return left-top's view
     */
    fun onCreateTopLeftView(): View

    /**
     * Return the view type of the item at `verticalPosition's` row
     * `horizontalPosition's` column for the purposes of view recycling.
     *
     *
     *
     * The default implementation of this method returns 2, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param verticalPosition   the row position to query
     * @param horizontalPosition the column position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * `verticalPosition's` row `horizontalPosition's` column.
     * Type codes need not be contiguous.
     */
    fun getCellItemViewType(verticalPosition: Int, horizontalPosition: Int): Int

    fun getTopItemViewType(position: Int): Int

    fun getLeftItemViewType(position: Int): Int

    /**
     * use to record the height of the row
     *
     * @param holder     cell's holder
     * @param position horizontal or vertical position
     */
    fun onAfterBind(holder: RecyclerView.ViewHolder, position: Int)

    fun fastScrollVertical(amountAxisY: Int, recyclerView: RecyclerView)
}