package com.sam.gogoeat.data.place

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.getDinstance
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class PlaceData(
    val business_status: String? = null,
    val geometry: Geometry? = null,
    val icon: String? = null,
    val icon_background_color: String? = null,
    val icon_mask_base_uri: String? = null,
    val name: String = "",
    val opening_hours: OpeningHours? = null,
    val photos: @RawValue List<Photo>? = null,
    val place_id: String? = null,
    val plus_code: PlusCode? = null,
    val rating: Double = 0.0,
    val reference: String? = null,
    val scope: String? = null,
    val types: @RawValue List<String>? = null,
    val user_ratings_total: Int = 0,
    val vicinity: String? = null
): Parcelable {
    val storeLatlng: LatLng
        get() = LatLng(geometry?.location?.lat ?: 25.0 , geometry?.location?.lng ?: 121.0)

    val distance: Int
        get() = UserManager.myLocation.getDinstance(storeLatlng)
}