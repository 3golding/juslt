package com.juslt.common.utils

import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import java.util.regex.Pattern

/**
 * Created by wx on 2018/6/13.
 */
object PhoneUtil{
    /**
     * 判断手机号是否符合规范
     * @param phoneNo 输入的手机号
     * @return
     */
    fun isPhoneNumber(phoneNo:String ):Boolean {
        if (TextUtils.isEmpty(phoneNo)) {
            return false
        }
        if (phoneNo.length == 11) {
            for ( i in 0..10) {
                if (!PhoneNumberUtils.isISODigit(phoneNo[i])) {
                    return false
                }
            }
            val  p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$")
            val  m = p.matcher(phoneNo)
            return m.matches()
        }
        return false
    }

    //隐藏中间四位的手机号
    fun getEncryptPhone(phone:String) :String{
        if(phone.length==11){
            val prefix = phone.substring(0,3)
            val suffix = phone.substring(7,11)
            return "$prefix****$suffix"
        }
        return phone
    }

}