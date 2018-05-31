package com.juslt.common.widget.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.juslt.common.R
import org.jetbrains.anko.find

/**
 * Created by Administrator on 2018/5/31.
 */
class JusltDialogEdit:DialogFragment(){
    var callback:Callback ?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_edit,container,false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tvCancel = view!!.find<TextView>(R.id.tv_cancel)
        val tvConfirm = view!!.find<TextView>(R.id.tv_confirm)
        val editText = view!!.find<EditText>(R.id.editText)
        tvCancel.setOnClickListener { dismiss() }
        tvConfirm.setOnClickListener {
            if(editText.text.toString().isNotEmpty()){
                callback!!.getEditContent(editText.text.toString())
                dismiss()
            }
        }
    }
    fun setCallBack(callback: Callback){
        this.callback=callback
    }
    interface Callback{
        fun getEditContent(content:String)
    }
}