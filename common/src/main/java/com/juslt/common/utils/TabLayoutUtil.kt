package com.juslt.common.utils

import android.content.res.Resources
import android.support.design.widget.TabLayout
import android.util.TypedValue
import android.widget.LinearLayout
import java.lang.reflect.Field

/**
 * Created by Administrator on 2018/6/4.
 */
class TabLayoutUtil {
    companion object {
        fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
            val tabLayout = tabs.javaClass
            var tabStrip: Field? = null
            try {
                tabStrip = tabLayout.getDeclaredField("mTabStrip")
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }

            tabStrip!!.isAccessible = true
            var llTab: LinearLayout? = null
            try {
                llTab = tabStrip.get(tabs) as LinearLayout
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

            val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().getDisplayMetrics())
            val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().getDisplayMetrics())

            for (i in 0 until llTab!!.childCount) {
                val child = llTab.getChildAt(i)
                child.setPadding(0, 0, 0, 0)
                val params = LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                params.leftMargin = left.toInt()
                params.rightMargin = right.toInt()
                child.layoutParams = params
                child.invalidate()
            }


        }

    }
}