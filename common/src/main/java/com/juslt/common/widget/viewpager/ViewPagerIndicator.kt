package com.juslt.common.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout

import com.juslt.common.R

/**
 * Created by wx on 2017/9/1.
 */

class ViewPagerIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var mContext: Context? = null
    private var selected: Int = 0

    init {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }

    fun setSelected(selected: Int) {
        this.selected = selected
        val childCount = childCount
        for (i in 0 until childCount) {
            val imageView = getChildAt(i) as ImageView
            if (selected == i) {
                imageView.setImageResource(R.drawable.point_blue)
            } else {
                imageView.setImageResource(R.drawable.point_white)
            }
        }
    }

    fun initPointNum(num: Int) {
        for (i in 0 until num) {
            val imageView = ImageView(mContext)
            imageView.setPadding(8, 0, 8, 0)
            imageView.setImageResource(R.drawable.point_white)
            addView(imageView)
        }
    }

}
