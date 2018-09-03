package com.juslt.common.widget.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.juslt.common.R
import org.jetbrains.anko.find

/**
 * Created by wx on 2018/6/12.
 */
class JusltConfirmDialog : DialogFragment() {
    var callbacks:Callback ?= null
    companion object {
        fun getInstance(title: String = "", content: String = "",confirmString: String="",cancelString:String =""):JusltConfirmDialog {
            val dialog = JusltConfirmDialog()
            val bundle = Bundle()
            bundle.putString("title",title)
            bundle.putString("content",content)
            bundle.putString("confirm",confirmString)
            bundle.putString("cancel",cancelString)
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
        val confirmString = bundle.getString("confirm")
        val cancelString = bundle.getString("cancel")
        view!!.findViewById<TextView>(R.id.tv_title).text = title
        view!!.findViewById<TextView>(R.id.tv_content).text=content
        if(content.isEmpty()){
            view!!.findViewById<TextView>(R.id.tv_content).visibility = View.GONE
        }
        if(confirmString.isNotEmpty()){
            view!!.find<TextView>(R.id.tv_confirm).text = confirmString
        }
        if(cancelString.isNotEmpty()){
            view!!.find<TextView>(R.id.tv_cancel).text = cancelString
        }
        view!!.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismissAllowingStateLoss()
            callbacks!!.cancel()
        }
        view!!.findViewById<TextView>(R.id.tv_confirm).setOnClickListener {
            dismissAllowingStateLoss()
            callbacks!!.confirm()
        }
    }


    interface Callback{
        fun confirm()
        fun cancel()
    }
}