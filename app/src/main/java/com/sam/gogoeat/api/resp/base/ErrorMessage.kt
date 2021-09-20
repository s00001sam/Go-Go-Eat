package com.sam.gogoeat.api.resp.base

import android.util.Log

class ErrorMessage {

    var code = 0

    var message: String? = null

    var exception: Throwable? = null

    fun ErrorMessage() {}

    fun ErrorMessage(message: String?) {
        this.message = message
    }

    fun ErrorMessage(code: Int, message: String?) {
        this.code = code
        this.message = message
    }

    fun ErrorMessage(code: Int, message: String?, exception: Throwable?) {
        this.code = code
        this.message = message
        this.exception = exception
    }

    override fun toString(): String {
        return "ErrorMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", exception=" + Log.getStackTraceString(exception) +
                "}"
    }
}