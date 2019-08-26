package at.technikumwien.maps.data.remote

import at.technikumwien.maps.data.model.DrinkingFountain

class MockDrinkingFountainApi : DrinkingFountainApi {

    override suspend fun getDrinkingFountains(): List<DrinkingFountain> =
        (0 until 10).map { i ->
            val latLngDiff = i / 100.0
            DrinkingFountain(
                i.toString(),
                "Trinkbrunnen $i",
                48.2306 - latLngDiff,
                16.3317 + latLngDiff
            )
        }

}