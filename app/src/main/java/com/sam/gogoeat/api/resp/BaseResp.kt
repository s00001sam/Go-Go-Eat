package com.sam.gogoeat.api.resp

import com.sam.gogoeat.api.resp.base.IResp
import com.sam.gogoeat.api.resp.base.RespStatus
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * 基本Resp
 */
class BaseResp<T> : IResp<T>, Serializable {
    @Json(name = "status")
    var status: RespStatus? = null

    @Json(name = "data")
    var data: T? = null


    fun isSuccess() = status?.isSuccess == true

    override fun toString(): String {
        return "BaseResp{" +
                "status=" + status +
                ", data=" + data +
                '}'
    }

    companion object {
        const val MSG_SUCCESS = "success."
        const val MSG_ERROR_JSON_ERROR = "json data parser error."
    }
}