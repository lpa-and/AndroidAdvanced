package at.technikumwien.maps.data.model

import com.google.android.gms.maps.model.LatLng

data class DrinkingFountain(
    val id: String,
    val name: String,
    val lat: Double,
    val lng: Double
) {

    val position: LatLng
        get() = LatLng(lat, lng)
}
