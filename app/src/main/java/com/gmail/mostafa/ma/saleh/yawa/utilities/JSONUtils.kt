package com.gmail.mostafa.ma.saleh.yawa.utilities

import android.content.res.Resources
import android.support.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter

object JSONUtils {

    fun readFromResources(resources: Resources, @RawRes resId: Int): String {
        val inputStream = resources.openRawResource(resId)
        val writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n: Int = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return writer.toString()
    }

}
