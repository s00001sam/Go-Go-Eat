package com.sam.gogoeat.api.resp

import com.sam.gogoeat.api.resp.base.IResp
import com.squareup.moshi.Json
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class NewsResp<T> (
        val status: String? = null,
        val totalResults: Int? = null,
        @Json(name = "articles") val data: @RawValue T? = null,
        val code: String? = null,
        val message: String? = null
): IResp<T>, Serializable {

    fun isSuccess() = status == "ok"

    override fun toString(): String {
        return "newsResp{" +
                "status=" + status +
                ", data=" + data +
                "}"
    }

    companion object {
        const val MSG_SUCCESS = "success."
        const val MSG_ERROR_JSON_ERROR = "json data parser error."
    }
}