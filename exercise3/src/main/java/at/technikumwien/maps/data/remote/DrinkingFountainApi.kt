package at.technikumwien.maps.data.remote

import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse
import retrofit2.http.GET

interface DrinkingFountainApi {

    @GET(GET_PATH)
    suspend fun getDrinkingFountains(): DrinkingFountainResponse

    companion object {
        const val BASE_URL = "https://data.wien.gv.at/daten/"
        const val GET_PATH = "geo?service=WFS&request=GetFeature&version=1.1.0&typeName=ogdwien:TRINKBRUNNENOGD&srsName=EPSG:4326&outputFormat=json"
    }
}
