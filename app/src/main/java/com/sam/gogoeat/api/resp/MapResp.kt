package com.sam.gogoeat.api.resp

import com.sam.gogoeat.api.resp.base.IResp
import com.squareup.moshi.Json
import kotlinx.android.parcel.RawValue
import java.io.Serializable

data class MapResp<T> (
        val status: String? = null,
        @Json(name = "results") val data: @RawValue T? = null,
        val error_message: String? = null
): IResp<T>, Serializable {

    fun isSuccess() = status == "OK"

    override fun toString(): String {
        return "MapResp : {" +
                "status=" + status +
                ", data=" + data +
                "}"
    }
}