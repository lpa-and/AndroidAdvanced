package at.technikumwien.maps.data.remote

import at.technikumwien.maps.data.remote.response.DrinkingFountainItem
import at.technikumwien.maps.data.remote.response.DrinkingFountainResponse
import at.technikumwien.maps.data.remote.response.Geometry
import at.technikumwien.maps.data.remote.response.Properties
import at.technikumwien.maps.util.CoroutineDownloader
import org.json.JSONObject

class HttpUrlConnectionDrinkingFountainApi : DrinkingFountainApi {

    override suspend fun getDrinkingFountains(): DrinkingFountainResponse =
        CoroutineDownloader.download(DrinkingFountainApi.BASE_URL + DrinkingFountainApi.GET_PATH) { json ->
            val jsonObject = JSONObject(json)
            val features = jsonObject.getJSONArray("features")

            val drinkingFountainItems = ArrayList<DrinkingFountainItem>(features.length())

            for (i in 0 until features.length()) {
                val drinkingFountainItem = features.getJSONObject(i)
                val geometry = drinkingFountainItem.getJSONObject("geometry")
                val coordinates: Array<Double> = geometry.getJSONArray("coordinates")
                    .let { ja -> Array(ja.length()) { index -> ja.getDouble(index) } }
                val properties = drinkingFountainItem.getJSONObject("properties")
                val id = drinkingFountainItem.getString("id")
                val name = properties.getString("NAME")
                drinkingFountainItems.add(
                    DrinkingFountainItem(id, Geometry(coordinates), Properties(name))
                )
            }

            DrinkingFountainResponse(drinkingFountainItems)
        }


}