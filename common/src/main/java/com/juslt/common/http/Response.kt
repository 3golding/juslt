package com.juslt.common.http

/**
 * Created by phelps on 2018/1/20 0020.
 */
class Response(
        val readSuccess: Boolean,
        val downloadPath: String?,
        val url: String,
        val responseCode: Int,
        val responseMessage: String,
        val header:Map<String,List<String>>?=null,
        val contentEncoding: String,
        val contentLength: Int,
        val data: String?=null,
        val requestStr: String
){

    override fun toString(): String {
        return buildString {
            appendln(requestStr+"\n")
            appendln("Response")
            appendln("url=$url")
            appendln("responseCode=$responseCode")
            appendln("responseMessage=$responseMessage")
            appendln("header=$header")
            appendln("date=$data")
            appendln("contentEncoding=$contentEncoding")
            appendln("contentLength=$contentLength")
        }
    }

    fun success(f: Response.()->Unit) : Response{
        if(responseCode==200 && readSuccess){
            f()
        }
        return this
    }

    fun err(f: Response.()->Unit) : Response{
        if(responseCode!=200 || !readSuccess){
            f()
        }
        return this
    }
}