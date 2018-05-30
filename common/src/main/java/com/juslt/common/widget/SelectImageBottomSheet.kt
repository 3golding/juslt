package com.juslt.common.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.juslt.common.R

/**
 * Created by wx on 2018/5/30.
 */
class SelectImageBottomSheet(context: Context,val callback:Callback):BottomSheetDialog(context){
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_image,null)
        setContentView(view)
        findViewById<TextView>(R.id.tvSelectCamera)!!.setOnClickListener {
            callback.openCamera()
            dismiss()
        }
        findViewById<TextView>(R.id.tvSelectGallery)!!.setOnClickListener {
            callback.selectGallery()
            dismiss()
        }
        findViewById<TextView>(R.id.tvCancel)!!.setOnClickListener {
            dismiss()
        }
    }
    interface Callback{
        fun openCamera()
        fun selectGallery()
    }
}