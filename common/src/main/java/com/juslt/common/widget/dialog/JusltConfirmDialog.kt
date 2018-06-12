package com.juslt.common.widget.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.juslt.common.R

/**
 * Created by wx on 2018/6/12.
 */
class JusltConfirmDialog : DialogFragment() {
    var callbacks:Callback ?= null
    companion object {
        fun getInstance(title: String = "", content: String = ""):JusltConfirmDialog {
            val dialog = JusltConfirmDialog()
            val bundle = Bundle()
            bundle.putString("title",title)
            bundle.putString("content",content)
            dialog.arguments = bundle
            return dialog
        }
    }
    fun setCallback(callback:Callback){
        this.callbacks = callback
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater!!.inflate(R.layout.dialog_confirm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = arguments
        val title = bundle.getString("title")
        val content = bundle.getString("content")
        view!!.findViewById<TextView>(R.id.tv_title).text = title
        view!!.findViewById<TextView>(R.id.tv_content).text=content
        view!!.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        view!!.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            callbacks!!.confirm()
        }
    }

    interface Callback{
        fun confirm()
    }
}