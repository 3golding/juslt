package com.juslt.common.widget.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.juslt.common.R
import org.jetbrains.anko.find
import java.util.*


/**
 * Created by Administrator on 2018/6/5.
 */
class JusltDatePickerDialog :DialogFragment(){
    val datePicker by lazy { view!!.find<DatePicker>(R.id.data_picker) }
    var callback:Callback?=null
    fun setCallBack(callback:Callback){
        this.callback = callback
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.dialog_data_picker,null)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        datePicker.init(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH) { view, year, monthOfYear, dayOfMonth ->
            callback!!.callback(year.toString() + "-" +monthOfYear+"-"+dayOfMonth)
            dismiss()
        }
    }
    interface Callback{
        fun callback(date:String)
    }


}