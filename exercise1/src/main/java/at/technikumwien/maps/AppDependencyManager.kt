package at.technikumwien.maps

import android.content.Context
import android.content.res.Resources
import at.technikumwien.maps.data.remote.DrinkingFountainApi
import at.technikumwien.maps.data.remote.MockDrinkingFountainApi

class AppDependencyManager(val appContext: Context) {

    val resources: Resources get() = appContext.resources

    val drinkingFountainApi: DrinkingFountainApi by lazy { MockDrinkingFountainApi() }

}
