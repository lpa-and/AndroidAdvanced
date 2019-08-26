package at.technikumwien.maps.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.technikumwien.maps.AppDependencyManager
import at.technikumwien.maps.MyApplication

class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val dependencyManager: AppDependencyManager =
        (context.applicationContext as MyApplication).appDependencyManager
    private val syncManager: SyncManager = dependencyManager.syncManager

    override suspend fun doWork(): Result =
        try {
            syncManager.syncDrinkingFountains()
            Result.success()
        } catch (exception: Exception) {
            Log.e("SyncWorker", "Could not sync drinking fountains, scheduling retry", exception)
            Result.retry()
        }
}