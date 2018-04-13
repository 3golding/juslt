package com.juslt.common.http

/**
 * Created by phelps on 2018/1/21 0021.
 */
interface IDownload{

    fun onProgress(current: Long, total: Long)
    fun onFinish()
    fun onError(e: Exception)

}