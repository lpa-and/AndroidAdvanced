package at.technikumwien.maps

import android.app.Application

class MyApplication : Application() {

    lateinit var appDependencyManager: AppDependencyManager
        private set

    override fun onCreate() {
        super.onCreate()
        appDependencyManager = AppDependencyManager(this)
        appDependencyManager.syncManager.schedulePeriodicSync()
    }
}
