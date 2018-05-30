package com.juslt.common.widget.label

import android.content.Context
import android.graphics.Color
import android.support.design.widget.BottomSheetDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayout
import com.juslt.common.R


/**
 * Created by wx on 2018/5/30.
 */
class LabelBottomSheet(context: Context, val nameList:List<String>, val callBack: CallBack) : BottomSheetDialog(context) {
    val labelBeanList = ArrayList<LabelBean>()
    //单选  false 多选
    private val singleSelected = true
    val tvConfirm by lazy { findViewById<TextView>(R.id.tv_confirm) }
    val fblContent by lazy { findViewById<FlexboxLayout>(R.id.fbl_content) }
    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_label, null)
        setContentView(view)

        initList()
        addLabelView()
        tvConfirm!!.setOnClickListener {
            val backLabelBean = ArrayList<LabelBean>()
            labelBeanList.forEach { it ->
                if (it.isSelected) {
                    backLabelBean.add(it)
                }
            }
            callBack.callback(backLabelBean)
            dismiss()
        }
    }


    private fun addLabelView() {
        fblContent!!.removeAllViews()
        labelBeanList.forEachIndexed { i, it ->
            val textView = createBaseFlexItemTextView(it)
            textView!!.layoutParams = createDefaultLayoutParams()
            textView.textSize = 13f
            textView.tag = i
            fblContent!!.addView(textView)
        }
    }

    private fun createDefaultLayoutParams(): ViewGroup.LayoutParams? {
        val lp = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 40, 40, 0)
        return lp
    }

    private fun createBaseFlexItemTextView(it: LabelBean): TextView? {
        val textView = TextView(context)
        textView.text = it.name
        textView.setPadding(30, 8, 30, 8)
        textView.gravity = Gravity.CENTER
        if (it.isSelected) {
            textView.setTextColor(Color.WHITE)
            textView.setBackgroundResource(R.drawable.selected_textbg)
        } else {
            textView.setTextColor(Color.parseColor("#535353"))
            textView.setBackgroundResource(R.drawable.unselected_textbg)
        }
        if (singleSelected) {
            dealSingle(textView, it)
        } else {
            selectedMore(textView, it)
        }
        return textView
    }

    private fun selectedMore(textView: TextView, label: LabelBean) {
        textView.setOnClickListener {
            if (label.isSelected) {
                label.isSelected = false
                textView.setTextColor(Color.parseColor("#535353"))
                textView.setBackgroundResource(R.drawable.unselected_textbg)
            } else {
                label.isSelected = true
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.selected_textbg)
            }
        }
    }

    private fun dealSingle(textView: TextView, label: LabelBean) {
        textView.setOnClickListener {
            textView.setTextColor(Color.parseColor("#535353"))
            textView.setBackgroundResource(R.drawable.unselected_textbg)
            val tag = textView.tag as Int
            if (label.isSelected) {
                label.isSelected = false
            } else {
                label.isSelected = true
                textView.setTextColor(Color.WHITE)
                textView.setBackgroundResource(R.drawable.selected_textbg)
                for (j in 0 until fblContent!!.childCount) {
                    val child = fblContent!!.getChildAt(j) as TextView
                    if (child.tag as Int != tag) {
                        labelBeanList[j].isSelected = false
                        child.setTextColor(Color.parseColor("#535353"))
                        child.setBackgroundResource(R.drawable.unselected_textbg)
                    }
                }
            }
        }
    }

    private fun initList() {
        nameList.forEachIndexed { index, s ->
            val labelBean = LabelBean(s, index, false)
            labelBeanList.add(labelBean)
        }
    }

    interface CallBack {
        fun callback(nameList: ArrayList<LabelBean>)
    }
}
data class LabelBean(
        val name:String,
        val id:Int,
        var isSelected:Boolean
)