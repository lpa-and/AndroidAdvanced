package at.technikumwien.maps.sync

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import at.technikumwien.maps.AppDependencyManager
import at.technikumwien.maps.data.local.DrinkingFountainDao
import at.technikumwien.maps.data.remote.DrinkingFountainApi
import java.util.concurrent.TimeUnit

private const val SHARED_PREFS_NAME = "syncPrefs"
private const val KEY_LAST_SYNC = "lastSync"
private const val MINIMUM_RESYNC_DURATION_MILLIS = 1 * 60 * 60 * 1000L // 1 hour

class SyncManager(
    private val context: Context,
    private val drinkingFountainApi: DrinkingFountainApi,
    private val drinkingFountainDao: DrinkingFountainDao
) {

    constructor(dependencyManager: AppDependencyManager) : this(
        dependencyManager.appContext,
        dependencyManager.drinkingFountainApi,
        dependencyManager.drinkingFountainDao
    )

    /** Use [SharedPreferences] to store and fetch the timestamp of the last sync */
    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    private var lastSync: Long
        get() = sharedPrefs.getLong(KEY_LAST_SYNC, 0)
        set(value) = sharedPrefs.edit().putLong(KEY_LAST_SYNC, value).apply()

    /** Only sync when at least [MINIMUM_RESYNC_DURATION_MILLIS] have passed since last sync */
    private val shouldSync: Boolean
        get() = lastSync + MINIMUM_RESYNC_DURATION_MILLIS < System.currentTimeMillis()

    suspend fun syncDrinkingFountains(force: Boolean = false) {
        if (force || shouldSync) {
            Log.d("SyncManager", "Starting sync")
            // Load drinking fountains from API and store them locally via the DAO
            val drinkingFountains = drinkingFountainApi.getDrinkingFountains().drinkingFountains
            drinkingFountainDao.refresh(drinkingFountains)

            // Set lastSync to current time
            lastSync = System.currentTimeMillis()
        } else {
            Log.d("SyncManager", "Data is still fresh, no sync needed")
        }
    }

    // Schedule a Worker that should be run every 7 days to sync drinking fountains in the background
    fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequest.Builder(SyncWorker::class.java, 7, TimeUnit.DAYS)
            .setInitialDelay(7, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(context).enqueue(syncWorkRequest)
    }
}