package com.atar.mango.ui.adapters

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class GridItemTouchHelper(private val mCallback: Callback) : ItemTouchHelper.Callback() {

    companion object {
        const val DRAGGED_ALPHA = 0.8f
        const val SETTLE_ALPHA = 1.0f
        const val ANIMATE_ALPHA_DURATION = 150L
    }

    /**
     * Data
     */
    private var dragFromPosition = -1
    private var dragToPosition = -1

    /**
     * ItemTouchHelper.Callback Methods
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags =
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        dragFromPosition = viewHolder.adapterPosition
        dragToPosition = target.adapterPosition
        mCallback.onMove(dragFromPosition, dragToPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mCallback.onSwipe(viewHolder.adapterPosition)
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == 1) {
            viewHolder.itemView.alpha =
                SETTLE_ALPHA - abs(dX) / viewHolder.itemView.width.toFloat()
            viewHolder.itemView.translationX = dX
            return

        }
        super.onChildDraw(
            c, recyclerView, viewHolder,
            dX, dY, actionState, isCurrentlyActive
        )
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState != 0) {
            viewHolder?.itemView?.isClickable = false
            viewHolder?.itemView?.animate()?.alpha(DRAGGED_ALPHA)?.duration = ANIMATE_ALPHA_DURATION
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.isClickable = true
        viewHolder.itemView.animate().alpha(SETTLE_ALPHA).duration = ANIMATE_ALPHA_DURATION
    }

    override fun isLongPressDragEnabled(): Boolean {
        return mCallback.enableDrag()
    }

    /**
     * Classes
     */
    interface Callback {
        fun onMove(from: Int, to: Int)
        fun onSwipe(position: Int)
        fun enableDrag(): Boolean
    }

}