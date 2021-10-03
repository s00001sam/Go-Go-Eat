package com.sam.gogoeat.api.resp

import com.sam.gogoeat.api.resp.base.IResp
import com.squareup.moshi.Json
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class MapResp<T> (
        val status: String? = null,
        @Json(name = "results") val data: @RawValue T? = null,
        @Json(name = "next_page_token") val nextPageToken: String? = null,
        @Json(name = "error_message") val error: String? = null
): IResp<T>, Serializable {

    fun isSuccess() = status == "OK"

    override fun toString(): String {
        return "MapResp : {" +
                "status=" + status +
                ", data=" + data +
                ", next_page_token=" + (nextPageToken ?: "null") +
                ", error_message=" + (error ?: "null") +
                "}"
    }
}