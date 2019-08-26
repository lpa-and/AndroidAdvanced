package at.technikumwien.maps.data.remote

import at.technikumwien.maps.data.model.DrinkingFountain

interface DrinkingFountainApi {

    suspend fun getDrinkingFountains(): List<DrinkingFountain>

}
