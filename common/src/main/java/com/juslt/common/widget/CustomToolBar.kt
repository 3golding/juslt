package com.juslt.common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.juslt.common.R
import org.jetbrains.anko.find

/**
 * Created by wx on 2018/4/28.
 */
class CustomToolBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val tvTitle by lazy { find<TextView>(R.id.tv_title) }
    private val ivBack by lazy { find<ImageView>(R.id.iv_back) }
    private val vDivider by lazy { find<ImageView>(R.id.iv_divider) }
    private val ivRight by lazy { find<ImageView>(R.id.iv_right) }
    private val tvRightText by lazy { find<TextView>(R.id.tv_right_text) }

    private val llRightContainer by lazy { find<LinearLayout>(R.id.ll_right) }
    init {
        LayoutInflater.from(context).inflate(R.layout.v_toolbar, this)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)
        val titleNameStyle = typeArray.getString(R.styleable.CustomToolBar_title_name)
        val titleColorStyle = typeArray.getColor(R.styleable.CustomToolBar_title_name_color, Color.parseColor("#4c4c4c"))
        val icBackStyle = typeArray.getResourceId(R.styleable.CustomToolBar_left_icon,R.mipmap.ic_back)
        val rightTextStyle = typeArray.getString(R.styleable.CustomToolBar_right_text)
        val rightTextColorStyle = typeArray.getColor(R.styleable.CustomToolBar_right_text_color, Color.parseColor("#4c4c4c"))
        val rightIconStyle = typeArray.getResourceId(R.styleable.CustomToolBar_right_icon,1)

        val isShowDividerStyle = typeArray.getBoolean(R.styleable.CustomToolBar_is_show_divider,false)
        val backgroundStyle = typeArray.getColor(R.styleable.CustomToolBar_background_color,Color.parseColor("#ffffff"))
        val isShowBackIconStyle = typeArray.getBoolean(R.styleable.CustomToolBar_is_show_back_icon,true)
        typeArray.recycle()

        tvTitle.text = titleNameStyle
        tvTitle.setTextColor(titleColorStyle)
        ivBack.setImageResource(icBackStyle)
        setBackgroundColor(backgroundStyle)

        tvRightText.text = rightTextStyle
        tvRightText.setTextColor(rightTextColorStyle)
        /** 显示右侧icon*/
        if(rightIconStyle==1){
            ivRight.visibility= View.GONE
        }else{
            ivRight.setImageResource(rightIconStyle)
        }
        /** 底部分界线*/
        if(isShowDividerStyle){
            vDivider.visibility = View.VISIBLE
        }else{
            vDivider.visibility = View.GONE
        }

        if(isShowBackIconStyle){
            ivBack.visibility = View.VISIBLE
        }else{
            ivBack.visibility = View.GONE
        }
    }

    fun setTitleText(title:String){
        tvTitle.text = title
    }
    fun setBackListener(listener: OnClickListener) {
        ivBack.setOnClickListener(listener)
    }
    fun setRightOptionListener(listener:OnClickListener){
        llRightContainer.setOnClickListener(listener )
    }



}