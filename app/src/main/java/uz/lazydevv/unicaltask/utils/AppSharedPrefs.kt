package uz.lazydevv.unicaltask.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.lazydevv.unicaltask.models.AppM

class AppSharedPrefs(private val sharedPrefs: SharedPreferences) {

    var firstPageApps: List<AppM>? = null
        get() {
            val gsonString = sharedPrefs.getString(KEY_FIRST_PAGE_APPS, null)
            return gsonToList(gsonString)
        }
        set(value) {
            field = value
            sharedPrefs.edit {
                putString(KEY_FIRST_PAGE_APPS, listToGson(value))
            }
        }

    var secondPageApps: List<AppM>? = null
        get() {
            val gsonString = sharedPrefs.getString(KEY_SECOND_PAGE_APPS, null)
            return gsonToList(gsonString)
        }
        set(value) {
            field = value
            sharedPrefs.edit {
                putString(KEY_SECOND_PAGE_APPS, listToGson(value))
            }
        }

    private fun listToGson(list: List<AppM>?): String {
        return Gson().toJson(list)
    }

    private fun gsonToList(gson: String?): List<AppM>? {
        if (gson == null) return null

        val listType = object : TypeToken<List<AppM>>() {}
        return Gson().fromJson(gson, listType)
    }

    companion object {

        private const val KEY_FIRST_PAGE_APPS = "first_page_apps"
        private const val KEY_SECOND_PAGE_APPS = "second_page_apps"
    }
}