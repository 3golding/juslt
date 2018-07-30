package com.juslt.common.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

/**
 * Created by juslt on 2018/7/30.
 */
object PwdCheckUtil {

    /**
     * 规则1：至少包含大小写字母及数字中的一种
     * 是否包含
     *
     * @param str
     * @return
     */
    fun isLetterOrDigit(str: String): Boolean {
        var isLetterOrDigit = false//定义一个boolean值，用来表示是否包含字母或数字
        for (i in 0 until str.length) {
            if (Character.isLetterOrDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isLetterOrDigit = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isLetterOrDigit && str.matches(regex.toRegex())
    }

    /**
     * 规则2：至少包含大小写字母及数字中的两种
     * 是否包含
     *
     * @param str
     * @return
     */
    fun isLetterDigit(str: String): Boolean {
        var isDigit = false//定义一个boolean值，用来表示是否包含数字
        var isLetter = false//定义一个boolean值，用来表示是否包含字母
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            } else if (Character.isLetter(str[i])) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLetter && str.matches(regex.toRegex())
    }

    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    fun isContainAll(str: String): Boolean {
        var isDigit = false//定义一个boolean值，用来表示是否包含数字
        var isLowerCase = false//定义一个boolean值，用来表示是否包含字母
        var isUpperCase = false
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            } else if (Character.isLowerCase(str[i])) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true
            } else if (Character.isUpperCase(str[i])) {
                isUpperCase = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLowerCase && isUpperCase && str.matches(regex.toRegex())
    }

    /**
     * 判断EditText输入的数字、中文还是字母方法
     */
    fun whatIsInput(context: Context, edInput: EditText) {
        val txt = edInput.getText().toString()

        var p = Pattern.compile("[0-9]*")
        var m = p.matcher(txt)
        if (m.matches()) {
            Toast.makeText(context, "输入的是数字", Toast.LENGTH_SHORT).show()
        }
        p = Pattern.compile("[a-zA-Z]")
        m = p.matcher(txt)
        if (m.matches()) {
            Toast.makeText(context, "输入的是字母", Toast.LENGTH_SHORT).show()
        }
        p = Pattern.compile("[\u4e00-\u9fa5]")
        m = p.matcher(txt)
        if (m.matches()) {
            Toast.makeText(context, "输入的是汉字", Toast.LENGTH_SHORT).show()
        }
    }
}