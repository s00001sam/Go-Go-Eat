package com.sam.gogoeat.api.resp.base

import org.json.JSONObject
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {
    fun getErrorMsg(e: Exception): String {
        var error = "Unknown Error"

        when (e) {
            is SocketTimeoutException -> {
                error = "connection error!"
            }
            is ConnectException -> {
                error = "no internet access!"
            }
            is UnknownHostException -> {
                error = "no internet access!"
            }
        }

        if(e is HttpException){
            when(e.code()){
                502 -> {
                    error = "internal error!"
                }
                401 -> {
                    error =  "authentication error!"
                }
                400 -> {
                    error = parseException(e)
                }
            }
        }

        return error
    }

    private fun parseException(e: HttpException): String {
        val errorBody = e.response()?.errorBody()?.string()

        return try {//here you can parse the error body that comes from server
            JSONObject(errorBody!!).getString("message")
        } catch (_: Exception) {
            return "unexpected error!!ً"
        }
    }
}