package at.technikumwien.maps

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import at.technikumwien.maps.data.local.AppDatabase
import at.technikumwien.maps.data.local.DrinkingFountainDao
import at.technikumwien.maps.data.remote.DrinkingFountainApi
import at.technikumwien.maps.sync.SyncManager
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppDependencyManager(val appContext: Context) {

    val resources: Resources get() = appContext.resources

    // by lazy { } can be used to delay initialization of a val until it is first used
    val okHttpClient: OkHttpClient by lazy { OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build() }

    val gson: Gson by lazy { Gson() }

    val syncManager: SyncManager by lazy { SyncManager(this) }

    val appDatabase: AppDatabase by lazy { Room.databaseBuilder(appContext, AppDatabase::class.java, "mapsdb").build() }

    val drinkingFountainDao: DrinkingFountainDao by lazy { appDatabase.drinkingFountainDao() }

    val drinkingFountainApi: DrinkingFountainApi by lazy {
        // Uncomment to switch to HttpUrlConnection Downloader
        //return@lazy HttpUrlConnectionDrinkingFountainApi()

        val httpClientBuilder = okHttpClient.newBuilder()

        // Enable logging on debug builds
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().run {
                level = HttpLoggingInterceptor.Level.BODY
                httpClientBuilder.addInterceptor(this)
            }
        }

        Retrofit.Builder()
            .baseUrl(DrinkingFountainApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .callFactory(httpClientBuilder.build())
            .build()
            .create(DrinkingFountainApi::class.java)
    }

}
