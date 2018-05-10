package com.juslt.common.widget

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.juslt.common.R
import com.juslt.common.widget.wheel.DateArrayAdapter
import com.juslt.common.widget.wheel.DateNumericAdapter
import com.juslt.common.widget.wheel.OnWheelChangedListener
import com.juslt.common.widget.wheel.WheelView
import org.jetbrains.anko.find
import java.util.*

/**
 * Created by Administrator on 2018/5/10.
 */
class DatePickerDialog:DialogFragment(){
    private var curYear: Int = 0
    private var curMonth: Int = 0
    private var curDay: Int = 0
    private lateinit var mCalendar: Calendar

    private lateinit var mYearView: WheelView
    private lateinit var mMonthView: WheelView
    private lateinit var mDayView: WheelView

    private lateinit var mTvSure: TextView
    private lateinit var mTvCancle: TextView

    private var beginYear = 0
    private var endYear = 0

    private val mMonths = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    private val mDays = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31")
    var dialogCallBack:DialogCallBack ?= null

    companion object {
        fun getInstance(beginYear:Int,endYear: Int):DatePickerDialog{
            val dialog = DatePickerDialog()
            val bundle = Bundle()
            bundle.putInt("beginYear",beginYear)
            bundle.putInt("endYear",endYear)
            dialog.arguments=bundle
            return dialog
        }
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_year_month_day,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        beginYear = arguments.getInt("beginYear")
        endYear = arguments.getInt("endYear")
        build()
    }

    private fun build() {
        mCalendar = Calendar.getInstance()
        val listener = OnWheelChangedListener { wheel, oldValue, newValue ->
            updateDays(mYearView, mMonthView, mDayView) }

        curYear = mCalendar.get(Calendar.YEAR)
        if (beginYear == 0) {
            beginYear = curYear - 5
        }
        if (endYear == 0) {
            endYear = curYear
        }
        if (beginYear > endYear) {
            endYear = beginYear
        }

        //mYearView
        mYearView = view!!.find(R.id.wheelView_year)
        mYearView.setBackgroundResource(R.mipmap.transparent_bg)
        mYearView.setWheelBackground(R.mipmap.transparent_bg)
        mYearView.setWheelForeground(R.drawable.wheel_val_holo)
        mYearView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)
        mYearView.viewAdapter = DateNumericAdapter(context, beginYear, endYear, endYear - beginYear)
        mYearView.currentItem = endYear - beginYear
        mYearView.addChangingListener(listener)

        // mMonthView
        mMonthView = view!!.findViewById(R.id.wheelView_month)
        mMonthView.setBackgroundResource(R.mipmap.transparent_bg)
        mMonthView.setWheelBackground(R.mipmap.transparent_bg)
        mMonthView.setWheelForeground(R.drawable.wheel_val_holo)
        mMonthView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)
        curMonth = mCalendar.get(Calendar.MONTH)
        mMonthView.viewAdapter = DateArrayAdapter(context, mMonths, curMonth)
        mMonthView.currentItem = curMonth
        mMonthView.addChangingListener(listener)


        //mDayView
        mDayView = view!!.findViewById(R.id.wheelView_day)
        updateDays(mYearView, mMonthView, mDayView)
        curDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        mDayView.currentItem = curDay - 1
        mDayView.setBackgroundResource(R.mipmap.transparent_bg)
        mDayView.setWheelBackground(R.mipmap.transparent_bg)
        mDayView.setWheelForeground(R.drawable.wheel_val_holo)
        mDayView.setShadowColor(-0x252325, -0x77252325, 0x00DADCDB)

        mTvSure = view!!.findViewById(R.id.tv_sure)
        mTvCancle = view!!.findViewById(R.id.tv_cancel)

        mTvCancle.setOnClickListener {
            dialog.dismiss()
        }
        mTvSure.setOnClickListener {
            dialogCallBack!!.callback(
                    beginYear+mYearView.currentItem
                    ,mMonths[mMonthView.currentItem].toInt()
                    ,mDays[mDayView.currentItem].toInt())
        }

    }

    private fun updateDays(year: WheelView, month: WheelView, day: WheelView) {
        mCalendar.set(Calendar.YEAR, beginYear + year.currentItem)
        mCalendar.set(Calendar.MONTH, month.currentItem)
        val maxDay = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val maxDays = getDaysByYearMonth(beginYear + year.currentItem, month.currentItem + 1)
        day.viewAdapter = DateNumericAdapter(context, 1, maxDays, mCalendar.get(Calendar.DAY_OF_MONTH) - 1)
        val curDay = Math.min(maxDays, day.currentItem + 1)
        day.setCurrentItem(curDay - 1, true)
    }

    private fun getDaysByYearMonth(year: Int, month: Int): Int {
        val a = Calendar.getInstance()
        a.set(Calendar.YEAR, year)
        a.set(Calendar.MONTH, month - 1)
        a.set(Calendar.DATE, 1)
        a.roll(Calendar.DATE, -1)
        return a.get(Calendar.DATE)
    }
    fun setOnClickCallBack(callback:DialogCallBack){
        this.dialogCallBack = callback
    }
    interface DialogCallBack{
        fun callback(year:Int,month:Int,day:Int)
    }
}