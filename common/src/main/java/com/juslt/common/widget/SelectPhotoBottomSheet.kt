package com.juslt.common.widget

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.juslt.common.R
import com.juslt.common.rv.UIEventInterface
import org.jetbrains.anko.find

/**
 * Created by wx on 2018/5/15.
 */
class SelectPhotoBottomSheet(context: Context, uiEventInterface: UIEventInterface) : BottomSheetDialog(context) {
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_select_photo,null)
        setContentView(view)
        view.find<TextView>(R.id.tvSelectCamera).setOnClickListener {
            uiEventInterface.event(2,0)
            dismiss()
        }
        view.find<TextView>(R.id.tvSelectGallery).setOnClickListener {
            uiEventInterface.event(1,0)
            dismiss()
        }
        view.find<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
    }
}