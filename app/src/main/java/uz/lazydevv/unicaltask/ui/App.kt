package uz.lazydevv.unicaltask.ui

import android.app.Application
import android.content.Context
import uz.lazydevv.unicaltask.utils.AppSharedPrefs

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = AppSharedPrefs(getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE))
    }

    companion object {

        private const val SHARED_PREFS_NAME = "shared_prefs"
        lateinit var sharedPrefs: AppSharedPrefs
    }
}