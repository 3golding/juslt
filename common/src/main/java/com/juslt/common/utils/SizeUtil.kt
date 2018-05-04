package com.juslt.common.utils

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by wx on 2017/9/18.
 */

object SizeUtil {
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun getDisplayMetrics(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics
    }

    fun getRealmetrics(activity: Activity): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        return displayMetrics
    }

    fun getWindowWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    fun getWindowHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

}
