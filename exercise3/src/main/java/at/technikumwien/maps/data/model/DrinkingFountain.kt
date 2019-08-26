package at.technikumwien.maps.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import at.technikumwien.maps.data.model.DrinkingFountain.Companion.TABLE_NAME
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = TABLE_NAME)
data class DrinkingFountain(
    @PrimaryKey val id: String,
    val name: String,
    val lat: Double,
    val lng: Double
) {
    companion object {
        const val TABLE_NAME = "drinking_fountains"
    }

    val position: LatLng
        get() = LatLng(lat, lng)
}
