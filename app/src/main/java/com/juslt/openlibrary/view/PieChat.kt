package com.juslt.openlibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by wx on 2018/4/27.
 */
class PieChat @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    var paints = ArrayList<Paint>()
    val colors = ArrayList<Int>()
    val percentages = ArrayList<Float>()

    val circlePaint by lazy { Paint() }
    var isIncludeCenter = false

//    val colors = arrayListOf<Int>(R.color.colorPrimary,R.color.colorAccent)
//    val percentages = arrayListOf<Float>(30f,70f)

    fun initAttr(colorList:ArrayList<Int>,percentageList:ArrayList<Float>,isIncludeCenter:Boolean= false){
        this.isIncludeCenter =isIncludeCenter
        colors.clear()
        colors.addAll(colorList)
        percentages.clear()
        percentages.addAll(percentageList)

        circlePaint.color = Color.WHITE
        circlePaint.style = Paint.Style.FILL


        for(i in 0 until colors.size){
            val paint = Paint()
            paint.color =resources.getColor(colors[i])
            paint.style=Paint.Style.FILL
            paint.strokeWidth =4f
            paints.add(paint)
        }
//        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画布背景
        canvas!!.drawColor(Color.WHITE)

        Log.e("===", "left:$left,right:$right,top:$top,bottom:$bottom")
        val arcRectf= RectF(0f, 0f, width.toFloat(), height.toFloat())

        var curPer =0f
        var percentage = 0f
        for (i in 0 until colors.size){
            percentage = 360*(percentages[i]/100)
            percentage = ((Math.round(percentage *100))/100).toFloat()
            //显示所占比例
            canvas.drawArc(arcRectf,curPer,percentage,true,paints[i])
            curPer+=percentage
        }

        if(isIncludeCenter){
            val radius = (right-left)/4
            var cirX = left+radius*2
            var cirY = top+radius*2
            canvas.drawCircle(cirX.toFloat(),cirY.toFloat(),radius.toFloat(),circlePaint)
        }

    }
}