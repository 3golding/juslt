package com.juslt.openlibrary.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.juslt.common.utils.SizeUtil

/**
 * Created by wx on 2018/5/2.
 */
class LineChartView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var leftX = 0f
    private var topY = 0f
    private var rightX = 0f
    private var bottomY = 0f

    private val linePaint: Paint = Paint()
    private val textPaint: Paint = Paint()
    private val brokeLinePaint = Paint()
    private val brokeLinePaint2=Paint()
    private val mPath = Path()

    var index_x = arrayOf("01", "02", "03", "04", "05", "06", "07", "08", "")


    private var maxY =3000//Y轴数据最大值
    private var rulerY: FloatArray? = null// xy轴向刻度

    var spaceX =0f
    val pointFs = ArrayList<PointF>()
    val pointFs2 = ArrayList<PointF>()

    init {
        linePaint.color = Color.parseColor("#e6e6e6")
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 3f
        linePaint.isAntiAlias = true

        brokeLinePaint.color = Color.parseColor("#fdb741")
        brokeLinePaint.style = Paint.Style.STROKE
        brokeLinePaint.strokeWidth = SizeUtil.dip2px(context,2f).toFloat()
        brokeLinePaint.isAntiAlias = true

        brokeLinePaint2.color = Color.parseColor("#3dcc85")
        brokeLinePaint2.style = Paint.Style.STROKE
        brokeLinePaint2.strokeWidth = SizeUtil.dip2px(context,2f).toFloat()
        brokeLinePaint2.isAntiAlias = true


        textPaint.textSize = SizeUtil.sp2px(context, 13f).toFloat()
        textPaint.color =  Color.parseColor("#999999")

        pointFs.add(PointF(0.3F, 800F))
        pointFs.add(PointF(1F, 500F))
        pointFs.add(PointF(2F, 1200F))
        pointFs.add(PointF(3F, 1500F))
        pointFs.add(PointF(4F, 1600F))
        pointFs.add(PointF(5F, 2000F))
        pointFs.add(PointF(6F, 1800f))
        pointFs.add(PointF(7F, 1600F))

        pointFs2.add(PointF(0.3F, 700F))
        pointFs2.add(PointF(1F, 600F))
        pointFs2.add(PointF(2F, 1400F))
        pointFs2.add(PointF(3F, 1700F))
        pointFs2.add(PointF(4F, 1300F))
        pointFs2.add(PointF(5F, 1200F))
        pointFs2.add(PointF(6F, 2000f))
        pointFs2.add(PointF(7F, 1500F))

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val width = width
        leftX = (width / 16*2).toFloat()
        rightX = (width * 15 / 16).toFloat()
        topY = (width / 16).toFloat()
        bottomY = (width * 1 / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.save()
        drawXY(canvas)
    }

    private fun drawXY(canvas: Canvas) {
        //绘制X Y 轴
        mPath.moveTo(leftX, topY)
        mPath.lineTo(leftX, bottomY)
        mPath.lineTo(rightX, bottomY)
        canvas.drawPath(mPath, linePaint)

        drawLine(canvas)
    }

    private fun drawLine(canvas: Canvas) {
        val countX = pointFs.size
        val countY = 6
//        val sc = canvas.saveLayerAlpha(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), 75, Canvas.ALL_SAVE_FLAG)
        spaceX= (rightX - leftX) / 8
        val spaceY = (bottomY - topY) / 5       // 绘制横纵线段

        // 计算除数的值为数据长度减一，8个数据，7条线。
        val divisor = countX - 1

        // 计算纵轴数据最大值
//        maxY = 3000
//        for (i in 0 until countX) {
//            if (maxY < pointFs[i].y) {
//                maxY = pointFs[i].y
//            }
//        }
//        Log.i("Text", "maxY:--$maxY")
//        // 计算纵轴最近的能被count整除的值
//        val remainderY = maxY.toInt() % divisor
//        //        Log.i("Text","remainderY:--"+remainderY);
//
//        if (remainderY == 0 && maxY == 0f) {
//            maxY = 0f
//        } else {
//            maxY = (divisor - remainderY + maxY.toInt()).toFloat()
//        }

        // 生成纵轴刻度值
        rulerY = FloatArray(countX)
        for (i in 0 until countX) {
            rulerY!![i] = (maxY / divisor * i).toFloat()
        }

        for (i in 0..countX) {
            canvas.drawLine(leftX + spaceX * i, topY, leftX + spaceX * i, bottomY, linePaint)
            canvas.drawText(index_x[i], leftX + spaceX * i, bottomY + SizeUtil.dip2px(context, 20f), textPaint)
        }
        for (i in 1..countY) {
            canvas.drawLine(leftX, spaceY * i + topY, rightX, spaceY * i + topY, linePaint)
            canvas.drawText((i*maxY/countY).toString(), 30f, bottomY - spaceY * i, textPaint)
        }
//
        canvas.restore()

        // 重置曲线
        mPath.reset()
        //绘制我们的坐标点
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        for (i in pointFs.indices) {
            // 计算x坐标
            val x = leftX+spaceX*i
            // 计算y坐标
            var y = bottomY / maxY * pointFs[i].y
            y = bottomY - y

            if (i == 0) {
                mPath.moveTo(x, y)
            }

            // 连接各点
            mPath.lineTo(x, y)
        }
        // 设置PathEffect
//        brokeLinePaint.setPathEffect(CornerPathEffect(10f))
//        // 重置线条宽度
//        brokeLinePaint.setStrokeWidth(2f)

        // 将Path绘制到我们自定的Canvas上
        canvas.drawPath(mPath, brokeLinePaint)


        mPath.reset()
        for (i in pointFs2.indices) {
            // 计算x坐标
            val x = leftX+spaceX*i
            // 计算y坐标
            var y = bottomY / maxY * pointFs2[i].y
            y = bottomY - y

            if (i == 0) {
                mPath.moveTo(x, y)
            }

            // 连接各点
            mPath.lineTo(x, y)
        }
        // 设置PathEffect
//        brokeLinePaint.setPathEffect(CornerPathEffect(10f))
//        // 重置线条宽度
//        brokeLinePaint.setStrokeWidth(2f)

        // 将Path绘制到我们自定的Canvas上
        canvas.drawPath(mPath, brokeLinePaint2)
    }
}