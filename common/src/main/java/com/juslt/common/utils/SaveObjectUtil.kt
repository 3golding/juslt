package com.juslt.common.utils

import android.content.Context
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Created by wx on 2018/5/24.
 */
object SaveObjectUtil {

    fun storeObject(context: Context, name:String,any:Any) {
        val os = context.openFileOutput(name,Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(os)
        objectOutputStream.writeObject(any)
        objectOutputStream.flush()
        objectOutputStream.close()
    }
    fun getStoreObject(context: Context,name: String):Any?{
        val fileInputStrem = context.openFileInput(name)
        val objectInputStream = ObjectInputStream(fileInputStrem)
        return objectInputStream.readObject()
    }
    fun delete(context: Context,name: String){
        val file = File(context.filesDir,name)
        file.delete()
    }

}