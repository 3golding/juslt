package com.juslt.common.rv

import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent

/**
 * Created by wx on 2017/11/21.
 */

abstract class OnRecyclerItemClickListener(private val recyclerView: RecyclerView) : RecyclerView.OnItemTouchListener {
    private val gestureDetectorCompat: GestureDetectorCompat

    init {
        gestureDetectorCompat = GestureDetectorCompat(recyclerView.context,
                ItemTouchHelperGestureListener())
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        gestureDetectorCompat.onTouchEvent(e)
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        gestureDetectorCompat.onTouchEvent(e)
        return false
    }

    abstract fun onItemClick(viewHolder: RecyclerView.ViewHolder)
    abstract fun onItemLongClick(viewHolder: RecyclerView.ViewHolder)
    internal inner class ItemTouchHelperGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val child = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val viewHolder = recyclerView.getChildViewHolder(child)
                onItemClick(viewHolder)
            }
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val child = recyclerView.findChildViewUnder(e.x, e.y)
            if (child != null) {
                val viewHolder = recyclerView.getChildViewHolder(child)
                onItemLongClick(viewHolder)
            }
        }
    }
}
