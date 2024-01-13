package com.sam.gogoeat.api.resp

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.Parcelize

@Parcelize
data class MapResp<T> (
        @Json(name = "status") val status: String? = null,
        @Json(name = "results") val data: @RawValue T? = null,
        @Json(name = "next_page_token") val nextPageToken: String? = null,
        @Json(name = "error_message") val error: String? = null
): Parcelable {

    fun isSuccess() = status == "OK"
}
