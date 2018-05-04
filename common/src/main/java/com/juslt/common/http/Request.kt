package com.juslt.common.http

import java.io.File
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Created by phelps on 2018/1/20 0020.
 */
class Request(
        internal val url: String,
        internal val method: String= Method.GET,
        internal val bodyParams: ArrayList<Pair<String,Any>> = ArrayList(),
        internal val headerParams: MutableMap<String,String> = mutableMapOf(),
        internal val connTimeout: Int = CONN_TIMEOUT,
        internal val readTimeout: Int = READ_TIMEOUT
){

    internal var downloadPath: String?=null
    internal var iDownload: IDownload?=null

    fun addHeader(pair: Pair<String, String>) : Request {
        headerParams.put(pair.first,pair.second)
        return this
    }

    fun addParam(pair: Pair<String,Any>) : Request {
        bodyParams.add(pair)
        return this
    }

    fun downloadTo(path: String,i: IDownload) : Request {
        downloadPath = path
        iDownload = i
        return this
    }

    fun exec() : Response {
        return when(method){
            Method.GET -> execGet()
            Method.POST -> execPost()
            else -> throw IllegalArgumentException("no method")
        }
    }

    private fun execPost() : Response {
        val conn = genConnection(URL(url), method)
        //post params
        val isMultipart = bodyParams.any { it.second is File }

        try {
            if (isMultipart) {
                processMultipart(conn)
            } else {
                processPostForm(conn)
            }
        } catch (e: Exception) {
            //io exception, connect timeout  etc.
            return readResponse(conn)
        }
        return readResponse(conn)
    }

    private fun processPostForm(conn: HttpURLConnection){
        //body
        val paramStr = StringBuffer()
        for ((first,second) in bodyParams) {
            if (paramStr.isEmpty()) {
                paramStr.append("$first=$second")
            } else {
                paramStr.append("&$first=$second")
            }
        }

        try {
            val os = conn.outputStream
            os.write(paramStr.toString().toByteArray())
            os.flush()
            os.close()
        }catch (e: Exception){
            throw e
        }

    }

    private fun processMultipart(conn: HttpURLConnection){
        val BOUNDARY = "BOUNDARY-" + java.util.UUID.randomUUID().toString()

        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY)
        conn.setChunkedStreamingMode(0)

        val os = conn.outputStream
        bodyParams.forEach {
            when {
                it.second is File -> writePart(it.first,it.second as File,os,BOUNDARY)
                it.second is String -> writePart(it.first,it.second as String,os,BOUNDARY)
                else -> throw IllegalArgumentException("param type error = ${it.second::class.java.name}")
            }
        }

        // 请求结束标志
        val endData = (PREFIX + BOUNDARY + PREFIX + LINEND).toByteArray()
        os.write(endData)
        os.flush()
    }

    private fun writePart(key: String, value: String,os: OutputStream, boundary: String){
        // 文本类型的参数
        val partStr = buildString {
            append(PREFIX)
            append(boundary)
            append(LINEND)
            append("Content-Disposition: form-data; name=\"" + key + "\"" + LINEND)
            append(LINEND)
            append(value)
            append(LINEND)
        }

        os.write(partStr.toByteArray())
    }

    private fun writePart(key: String, value: File, os: OutputStream, boundary: String){
        //写入文件
        val partStr = buildString {
            append(PREFIX)
            append(boundary)
            append(LINEND)
            append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + value.name + "\"" + LINEND)
            append("Content-Type: application/octet-stream" + LINEND)
            append(LINEND)
        }
        os.write(partStr.toByteArray())

        val fis = value.inputStream()
        var bytesCopied: Long = 0
        val buffer = ByteArray(1024)
        var bytes = fis.read(buffer)
        while (bytes >= 0) {
            os.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = fis.read(buffer)
        }

        fis.close()
        os.write(LINEND.toByteArray())
    }

    private fun execGet() : Response {
        val getUrl = buildString {
            append(url)
            bodyParams.forEachIndexed { index, pair ->
                val key = URLEncoder.encode(pair.first, "UTF-8")
                val value = URLEncoder.encode(pair.second.toString(), "UTF-8")
                if (index == 0) {
                    append("?$key=$value")
                } else {
                    append("&$key=$value")
                }
            }
        }
        val conn = genConnection(URL(getUrl),method)
        return readResponse(conn)
    }

    private fun genConnection(url: URL,method: String) : HttpURLConnection{
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = method
        conn.readTimeout = readTimeout
        conn.connectTimeout = connTimeout
        conn.useCaches = false
        conn.doInput = true
        conn.doOutput = method== Method.POST

        if(downloadPath!=null){
            conn.setRequestProperty("Accept-Encoding", "identity")
        }

        //header
        for ((first,second) in headerParams) {
            conn.setRequestProperty(first,second)
        }

        return conn
    }

    override fun toString(): String {
        return buildString {
            appendln("Request")
            appendln("url=$url")
            appendln("method=$method")
            appendln("header=$headerParams")
            appendln("body=$bodyParams")
        }
    }

    companion object {
        var READ_TIMEOUT = 30*1000
        var CONN_TIMEOUT = 30*1000

        private val PREFIX = "--"
        private val LINEND = "\r\n"
    }
}