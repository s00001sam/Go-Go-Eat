package com.sam.gogoeat.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.sam.gogoeat.utils.UserManager
import com.sam.gogoeat.utils.Util.getDinstance
import kotlinx.parcelize.Parcelize

@Entity(tableName = "place_table")
@Parcelize
data class GogoPlace(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val name: String = "",
    val place_id: String? = null,
    val rating: Double = 0.0,
    val user_ratings_total: Int = 0,
    val lat: Double? = null,
    val lng: Double? = null,
    val open_now: Boolean? = null
) : Parcelable {
    val storeLatlng: LatLng?
        get() {
            if (lat != null && lng!= null) return LatLng(lat, lng)
            else return null
        }

    val distance: Int
        get() {
            if (storeLatlng != null) return UserManager.mySettingData.myLocation.getDinstance(storeLatlng!!)
            else return 0
        }
}