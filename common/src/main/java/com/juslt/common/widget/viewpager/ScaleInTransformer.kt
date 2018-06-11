package com.juslt.common.widget.viewpager

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by wx on 2018/5/29.
 */
class ScaleInTransformer: ViewPager.PageTransformer{
    //大小缩放
    private val DEFAULT_MIN_SCALE = 0.85f
    private val mMinScale = DEFAULT_MIN_SCALE
    val DEFAULT_CENTER = 0.5f

    //透明度渐变
    private val DEFAULT_MIN_ALPHA = 0.5f
    private val mMinAlpha = DEFAULT_MIN_ALPHA
    override fun transformPage(view: View?, position: Float) {
        val pageWidth = view!!.width
        val pageHeight = view.height
        view.pivotX= (pageWidth/2).toFloat()
        view.pivotY = (pageHeight/2).toFloat()
        if(position<-1){
            view.scaleX=mMinScale
            view.scaleY=mMinScale
            view.pivotX= pageWidth.toFloat()
            view.alpha=mMinAlpha
        }else if (position<1){
            if(position<0){
                val scaleFactor = (1+position)*(1-mMinScale)+mMinScale
                view.scaleX=scaleFactor
                view.scaleY=scaleFactor
                view.pivotX=pageWidth*(DEFAULT_CENTER+(DEFAULT_CENTER*-position))

                val alphaFactor = mMinAlpha+(1-mMinAlpha)*(1+position)
                view.alpha=alphaFactor
            }else{
                val scaleFactor = (1-position)*(1-mMinScale)+mMinScale
                view.scaleX=scaleFactor
                view.scaleY=scaleFactor
                view.pivotX=pageWidth*((1-position)*DEFAULT_CENTER)

                val alphaFactor = mMinAlpha+(1-mMinAlpha)*(1-position)
                view.alpha=alphaFactor
            }
        }else{
            view.pivotX= 0F
            view.scaleX=mMinScale
            view.scaleY=mMinScale
            view.alpha=mMinAlpha
        }
    }

}