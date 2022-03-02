package com.sam.gogoeat.utils

import android.content.Context
import com.sam.gogoeat.data.GogoPlace
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.*

object FileUtil {

    fun writeToFile(data: String?, context: Context, fileName: String) {
        try {
            val file = File(context.cacheDir, fileName)
            val fileWriter = FileWriter(file)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(data)
            bufferedWriter.close()
        } catch (e: IOException) {
            Logger.e("File write failed: $e")
        }
    }

    fun readFromFile(context: Context, fileName: String): String {
        var ret = ""
        try {
            val file = File(context.cacheDir, fileName)
            val fileReader = FileReader(file)
            val bufferedReader = BufferedReader(fileReader)
            val stringBuilder = StringBuilder()
            var line = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
            ret = stringBuilder.toString()
        } catch (e: FileNotFoundException) {
            Logger.e("File not found: $e")
        } catch (e: IOException) {
            Logger.e("Can not read file: $e")
        }
        return ret
    }

    /**
     * Convert Json to [List] [String]
     */
    fun jsonToGogoPlaces(json: String?): List<GogoPlace>? {
        json?.let {
            val type = Types.newParameterizedType(List::class.java, GogoPlace::class.java)
            val adapter: JsonAdapter<List<GogoPlace>> =
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                    .adapter(type)
            return adapter.fromJson(it)
        }
        return null
    }

    fun gogoPlacesToJson(list: List<GogoPlace>?): String? {
        list?.let {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter<List<GogoPlace>>(List::class.java)
                .toJson(list)
        }
        return null
    }
}