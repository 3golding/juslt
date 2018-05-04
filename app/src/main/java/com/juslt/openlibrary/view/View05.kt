package com.juslt.openlibrary.view

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast


/**
 * Created by wx on 2018/5/3.
 */
class XYView05 @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {


    private var viewSize: Int = 0//获取空间的尺寸，也就是我们布局的尺寸大小（不知道理解的是否正确）
    private val linePaint = Paint()// 线条画笔和点画笔

    private var mCanvas: Canvas? = null

    private var mPath: Path? = null// 路径对象
    private val  mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.LINEAR_TEXT_FLAG)// 文字画笔

    private var pointFs: MutableList<PointF> = ArrayList()// 数据列表
    private var rulerX: FloatArray? = null
    private var rulerY: FloatArray? = null// xy轴向刻度

    //上下左右坐标点
    private var lift: Float = 0.toFloat()
    private var top: Float = 0.toFloat()
    private var right: Float = 0.toFloat()
    private var buttom: Float = 0.toFloat()

    //Y轴文字坐标点
    private var PathY_X: Float = 0.toFloat()
    private var PathY_Y: Float = 0.toFloat()
    //X轴文字坐标点
    private var PathX_X: Float = 0.toFloat()
    private var PathX_Y: Float = 0.toFloat()

    private var maxX: Float = 0.toFloat()//x轴最大值
    private var maxY: Float = 0.toFloat()//Y轴最大值

    private var spaceX: Float = 0.toFloat()
    private var spaceY: Float = 0.toFloat()// 刻度间隔

    /*
    * 绘制X和Y轴对应的文字
    * */
    internal var index_x = arrayOf("周一", "周二", "周三", "周四", "周五", "周六", "周日", "", "","")
    internal var index_y = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)


    init {

        //第一步，初始化对象

        linePaint.setColor(Color.YELLOW)//线条的颜色
        linePaint.setStrokeWidth(8f)//线条的宽度
        linePaint.setAntiAlias(true)//取消锯齿
        linePaint.setStyle(Paint.Style.STROKE)//粗线


        //初始化Path
        mPath = Path()


        mTextPaint.color = Color.WHITE

        mCanvas = Canvas()

        //模拟数据
        initData()
        pointFs.clear()
        pointFs.add(PointF(0.3F, 0.5F))
        pointFs.add( PointF(1F, 22.7F))
        pointFs.add( PointF(2F, 33.5F))
        pointFs.add( PointF(3F, 36.2F))
        pointFs.add( PointF(4F, 18.8F))
        pointFs.add( PointF(5F, 15.5F))
        pointFs.add( PointF(6F, 24.2F))
        pointFs.add( PointF(7F, 52.5F))
    }




    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //第二步骤，我们在这里获取每个用到的坐标点和尺寸

        viewSize = w//获取空间的尺寸，
        Log.i("Text", "viewSize:$viewSize")

        //这个是我们上下左右需要用到的坐标点
        lift = viewSize * (2 / 16f)
        top = viewSize * (2 / 16f)
        right = viewSize * (15 / 16f)
        buttom = viewSize * (8 / 16f)

        //下面是绘制X,Y轴提示文字
        /*
        * Y轴(PathY_X,PathY_Y)
        * */
        PathY_X = (viewSize * 2 / 16).toFloat()
        PathY_Y = (viewSize * 1 / 16).toFloat()

        /*
        * X轴(PathX_X,PathX_Y)
        * */
        PathX_X = viewSize * 15 / 16f
        PathX_Y = viewSize * 9 / 16f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 锁定画布
        canvas.save()


        //定义一个绘制X,Y轴的方法
        drawXY(canvas)


        //绘制X和Y轴上的提示文字
        drawXYelement(canvas)

        //最后绘制我们的点和线
        drawbitmaps(canvas)
        //
    }


    private fun initData() {
        pointFs = ArrayList()
        for (i in 0..7) {
            val pointF = PointF()

            pointF.x = 0.toFloat()
            pointF.y = index_y[i].toFloat()

            pointFs.add(pointF)
        }
    }

    private fun drawXY(canvas: Canvas) {
        /*
        * 第三步，我们来通过viewSize尺寸来获取三个坐标点
        * 第一个（X,Y）--(lift,top)
        * 第二个（X,Y）--(lift,button)
        * 第三个个（X,Y）--(right,buttom)
        * */
        mPath!!.moveTo(lift, top)
        mPath!!.lineTo(lift, buttom)
        mPath!!.lineTo(right, buttom)

        //使用Path链接这三个坐标
        canvas.drawPath(mPath, linePaint)

        //----------------------------我们在这里添加一个绘制网格的方法----------------------------------------
        drawLines(canvas)
        // 释放画布
        canvas.restore()
    }

    private fun drawLines(canvas: Canvas) {
        /*
        * 1、我们需要知道X,Y轴的最大值是多少
        * 2、我们需要知道我们在X,Y轴分别有多少个点，然后每个点之间的间距是多少
        * 3、绘制网格线
        * */

        // 重置线条画笔，因为是细线，所有我这里设置了2。
        linePaint.setStrokeWidth(2f)

        // 假如我们有八条数据
        val count = pointFs.size


        // 计算横纵坐标刻度间隔
        spaceY = (buttom - top) / count
        spaceX = (right - lift) / count

        // 计算除数的值为数据长度减一，8个数据，7条线。
        val divisor = count - 1

        // 计算横轴数据最大值
        maxX = 0f
        for (i in 0 until count) {
            if (maxX < pointFs[i].x) {
                maxX = pointFs[i].x//X轴最大坐标

            }
        }
        Log.i("Text", "maxX:--$maxX")
        // 计算横轴最近的能被count整除的值
        val remainderX = maxX.toInt() % divisor
        maxX = (if (remainderX == 0) maxX.toInt() else divisor - remainderX + maxX.toInt()).toFloat()


        // 计算纵轴数据最大值
        maxY = 0f
        for (i in 0 until count) {
            if (maxY < pointFs[i].y) {
                maxY = pointFs[i].y
            }
        }
        Log.i("Text", "maxY:--$maxY")
        // 计算纵轴最近的能被count整除的值
        val remainderY = maxY.toInt() % divisor
        //        Log.i("Text","remainderY:--"+remainderY);

        if (remainderY == 0 && maxY == 0f) {
            maxY = 0f
        } else {
            maxY = (divisor - remainderY + maxY.toInt()).toFloat()
        }

        // 生成横轴刻度值
        rulerX = FloatArray(count)
        for (i in 0 until count) {
            rulerX!![i] = maxX / divisor * i
        }
        //        Log.i("Text","rulerX:--"+rulerX);

        // 生成纵轴刻度值
        rulerY = FloatArray(count)
        for (i in 0 until count) {
            rulerY!![i] = maxY / divisor * i
        }

        // 锁定画布并设置画布透明度为75%
        val sc = canvas.saveLayerAlpha(0f, 0f, canvas.getWidth().toFloat(), canvas.getHeight().toFloat(), 75, Canvas.ALL_SAVE_FLAG)

        // 绘制横纵线段
        run {
            var y = buttom - spaceY
            while (y > top) {
                Log.i("Text", "y$y")

                var x = lift
                while (x < right) {
                    Log.i("Text", "x$x")
                    /*
                 * 绘制纵向线段
                 */
                    if (y == top + spaceY) {
                        canvas.drawLine(x, y, x, y + spaceY * (count - 1), linePaint)
                    }

                    /*
                 * 绘制横向线段
                 */
                    if (x == right - spaceX) {
                        canvas.drawLine(x, y, x - spaceX * (count - 1), y, linePaint)
                    }
                    x += spaceX
                }
                y -= spaceY
            }
        }

        // 还原画布
        canvas.restoreToCount(sc)


        var num = 0//用于给X轴赋值
        var num_y = 0//用于给Y轴赋值

        var y = buttom - spaceY
        while (y > top) {
            var x = lift
            while (x < right) {
                mTextPaint.textSize = 28f

                /*
                 * 绘制横轴刻度数值
                 */
                if (y == buttom - spaceY) {
                    canvas.drawText(index_x[num], x - 12, buttom + top / 3, mTextPaint)
                }
                /*
                 * 绘制纵轴刻度数值
                 * 简单来说就是，Y轴上的坐标点，X轴是恒定不变的，但注意是Y轴是变化的
                 */
                if (x == lift) {
                    canvas.drawText(rulerY!![num_y + 1].toInt().toString() + "", lift - lift / 2, y + 10, mTextPaint)

                }

                num++
                x += spaceX
            }
            num_y++
            y -= spaceY
        }
    }

    private fun drawXYelement(canvas: Canvas) {
        // 锁定画布
        canvas.save()
        mTextPaint.textSize = 36f//文字大小

        /*
        * Y轴文字提示
        * drawText(String ,x,y,TextPaint)
        * (lift,top)
        * */
        mTextPaint.textAlign = Paint.Align.LEFT//左对齐
        canvas.drawText("Y", PathY_X, PathY_Y, mTextPaint)


        /*
        * X轴文字提示
        * drawText(String ,right,buttom,TextPaint)
        * */
        mTextPaint.textAlign = Paint.Align.RIGHT//右对齐
        canvas.drawText("X", PathX_X, PathX_Y, mTextPaint)
        // 释放画布
        canvas.restore()
    }


    private fun drawbitmaps(canvas: Canvas) {
        /*
        我们给我们的区域先绘制一个颜色模块，做法很简单，生成一个图片即可，然后透明度设置下
        * Bitmap.createBitmap()
        * 关于他的6个方法，可查看博客：http://www.cnblogs.com/wangxiuheng/p/4503610.html
        * */


        val mBitmap = Bitmap.createBitmap((right - lift - spaceX).toInt(), (buttom - top - spaceY).toInt(), Bitmap.Config.ARGB_8888)

        mCanvas!!.setBitmap(mBitmap)

        /*
        * 为画布填充一个半透明的红色
        * drawARGB(a,r,g,b)
        * a:透明度
        * r:红色
        * g:绿色
        * b:蓝色
        * */
        mCanvas!!.drawARGB(55, 255, 0, 0)

        // 重置曲线
        mPath!!.reset()
        // 将mBitmap绘制到原来的canvas
        canvas.drawBitmap(mBitmap, lift, top + spaceY, null)


        //绘制我们的坐标点
        drawText(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        pointPaint.setStyle(Paint.Style.FILL)//焦点的类型
        pointPaint.setColor(Color.WHITE)//焦点的颜色

        if (pointFs.size == 0) {
            Toast.makeText(context, "暂无折现数据", Toast.LENGTH_SHORT).show()
        } else {
            /*
         * 生成Path和绘制Point
         */
            for (i in pointFs.indices) {
                // 计算x坐标
                val x =  mCanvas!!.getWidth() / maxX * pointFs[i].x
                // 计算y坐标
                var y =  mCanvas!!.getHeight() / maxY * pointFs[i].y
                y =  mCanvas!!.getHeight() - y

                // 绘制小点点
                mCanvas!!.drawCircle(x, y, 6f, pointPaint)

                /*
             * 如果是第一个点则将其设置为Path的起点
             */
                if (i == 0) {
                    mPath!!.moveTo(x, y)
                }

                // 连接各点
                mPath!!.lineTo(x, y)
            }

            // 设置PathEffect
            linePaint.setPathEffect(CornerPathEffect(10f))

            // 重置线条宽度
            linePaint.setStrokeWidth(4f)

            // 将Path绘制到我们自定的Canvas上
            mCanvas!!.drawPath(mPath, linePaint)
        }
    }


    @Synchronized
    fun setData(pointFs: MutableList<PointF>?, signX: String, signY: String, activity: Activity) {
        /*
         * 数据为空直接GG
         */
        if (null == pointFs || pointFs.size == 0)
            throw IllegalArgumentException("No data to display !")

        /*
         * 控制数据长度不超过10个
         * 对于折线图来说数据太多就没必要用折线图表示了而是使用散点图
         */
        if (pointFs.size > 10)
            throw IllegalArgumentException("The data is too long to display !")

        // 设置数据并重绘视图
        this.pointFs = pointFs

        invalidate()
    }
}