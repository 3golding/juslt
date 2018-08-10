package com.juslt.common.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

/**
 * Created by juslt on 2018/8/10.
 * 解决沉浸式状态栏界面下，EditText聚焦，软键盘弹起遮盖住界面的bug
 */
class RequestLayoutUtil private constructor(content: View?) {

    private var mChildOfContent: View? = null
    private var usableHeightPrevious: Int = 0
    private var frameLayoutParams: ViewGroup.LayoutParams? = null

    init {
        if (content != null) {
            mChildOfContent = content
            mChildOfContent!!.viewTreeObserver.addOnGlobalLayoutListener { possiblyResizeChildOfContent() }
            frameLayoutParams = mChildOfContent!!.layoutParams
        }
    }

    private fun possiblyResizeChildOfContent() {
        val usableHeightNow = computeUsableHeight()
        if (usableHeightNow != usableHeightPrevious) {
            //如果两次高度不一致
            //将计算的可视高度设置成视图的高度
            frameLayoutParams!!.height = usableHeightNow
            mChildOfContent!!.requestLayout()//请求重新布局
            usableHeightPrevious = usableHeightNow
        }
    }

    private fun computeUsableHeight(): Int {
        //计算视图可视高度
        val r = Rect()
        mChildOfContent!!.getWindowVisibleDisplayFrame(r)
        return r.bottom
    }

    companion object {

        fun assistActivity(content: View) {
            RequestLayoutUtil(content)
        }
    }

}