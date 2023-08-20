package uz.lazydevv.unicaltask.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.lazydevv.unicaltask.R
import uz.lazydevv.unicaltask.models.AppM
import uz.lazydevv.unicaltask.ui.App

class MainViewModel : ViewModel() {

    private val _sharedPrefs = App.sharedPrefs

    fun setupApps() {
        viewModelScope.launch {
            if (_sharedPrefs.firstPageApps == null) {
                getAppsList().apply {
                    _sharedPrefs.firstPageApps = subList(0, size / 2)
                }
            }

            if (_sharedPrefs.secondPageApps == null) {
                getAppsList().apply {
                    _sharedPrefs.secondPageApps = subList(size / 2, size)
                }
            }
        }
    }

    private fun getAppsList(): List<AppM> {
        return listOf(
            AppM(name = "USSD", icon = R.drawable.icon_ussd),
            AppM(name = "Black wallpapers", icon = R.drawable.icon_black_wallpapers),
            AppM(name = "Tasbeh", icon = R.drawable.icon_tasbeh),
            AppM(name = "Wallpa - 4K wallpapers", icon = R.drawable.icon_wallpa),
            AppM(name = "Clash of clans", icon = R.drawable.icon_clash),
            AppM(name = "Google calendar", icon = R.drawable.icon_calendar),
            AppM(name = "ShareIt", icon = R.drawable.icon_shareit),
            AppM(name = "Instagram", icon = R.drawable.icon_instagram),
            AppM(name = "Facebook", icon = R.drawable.icon_facebook),
            AppM(name = "islom.uz", icon = R.drawable.icon_islomuz),
            AppM(name = "Duolingo", icon = R.drawable.icon_duolingo),
        )
    }
}