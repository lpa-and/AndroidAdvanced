package at.technikumwien.maps.data.remote.response

import at.technikumwien.maps.data.model.DrinkingFountain

class DrinkingFountainResponse(
    private val features: List<DrinkingFountainItem>
) {
    val drinkingFountains: List<DrinkingFountain>
        get() = features.map { item ->
            DrinkingFountain(
                item.id,
                item.properties.NAME,
                item.geometry.coordinates[1],
                item.geometry.coordinates[0]
            )
        }
}

class DrinkingFountainItem(
    val id: String,
    val geometry: Geometry,
    val properties: Properties
)

class Geometry(
    val coordinates: Array<Double>
)

class Properties(
    val NAME: String
)