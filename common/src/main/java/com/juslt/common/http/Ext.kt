package com.juslt.common.http

import com.google.gson.Gson
import java.io.*
import java.net.HttpURLConnection


/**
 * Created by phelps on 2018/1/20 0020.
 */
fun String.hGet() : Request {
    return Request(this, Method.GET)
}

fun String.hPost() : Request {
    return Request(this, Method.POST)
}

fun <T> Response.parse(t: Class<T>) : T{
    return Gson().fromJson(data,t)
}

fun Request.readResponse(conn: HttpURLConnection) : Response{
    var dataStream: InputStream?=null
    var resultStr: String?=null
    var readSuccess = true
    try {
        //todo 处理gzip
        dataStream = conn.errorStream?:conn.inputStream?:ByteArrayInputStream(ByteArray(0))
        resultStr = if(downloadPath==null){
            String(dataStream.readBytes())
        }else{
            download(dataStream,conn.contentLength)
            "download"
        }
    } catch (ex: IOException) {
        readSuccess = false
        return Response(
                readSuccess,
                downloadPath,
                url,
                -1,
                "",
                null,
                "",
                -1,
                ex.message,
                toString()
        )
    }finally {
        dataStream?.close()
    }

    return Response(
            readSuccess,
            downloadPath,
            url,
            conn.responseCode,
            conn.responseMessage,
            conn.headerFields,
            conn.contentEncoding?:"",
            conn.contentLength,
            resultStr,
            toString()
    )
}

private fun Request.download(dataStream: InputStream,contentLength: Int) : ByteArray{
    try {

        val fos = FileOutputStream(File(downloadPath))

        var bytesCopied: Long = 0
        val buffer = ByteArray(1024)
        var bytes = dataStream.read(buffer)
        while (bytes >= 0) {
            fos.write(buffer, 0, bytes)
            bytesCopied += bytes
            iDownload?.onProgress(bytesCopied,contentLength.toLong())
            bytes = dataStream.read(buffer)
        }

        dataStream.close()
        fos.close()
        iDownload?.onFinish()
    }catch (e: Exception){
        iDownload?.onError(e)
    }finally {
        return ByteArray(0)
    }
}
