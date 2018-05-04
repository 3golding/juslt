package com.juslt.common.http

import android.util.Log
import com.google.gson.Gson


/**
 * Created by phelps on 2018/1/28 0028.
 */
inline fun <reified T> Response.parseSuccess(crossinline f:(t: T)->Unit) : Response{
    success {
        val from = Gson().fromJson<T>(data, T::class.java)
        f.invoke(from)
    }
    return this
}

inline fun Response.parseError(crossinline f:(t: ErrorVo)->Unit) : Response{
    err {
        f.invoke(ErrorVo(responseCode,data?:""))
    }
    return this
}

fun Response.log() : Response{
    Log.d("log_http",toString())

    return this
}