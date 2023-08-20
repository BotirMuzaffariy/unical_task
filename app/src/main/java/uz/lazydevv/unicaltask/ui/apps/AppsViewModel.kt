package uz.lazydevv.unicaltask.ui.apps

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.lazydevv.unicaltask.models.AppM
import uz.lazydevv.unicaltask.ui.App

class AppsViewModel : ViewModel() {

    private var _apps = MutableStateFlow<List<AppM>>(emptyList())
    val apps = _apps.asStateFlow()

    private var _fragmentPos = 0
    private var _reorderedApps = emptyList<AppM>()

    fun setFragmentPos(position: Int?) {
        _fragmentPos = position ?: 0
    }

    fun setReorderedApps(apps: List<AppM>) {
        _reorderedApps = apps
    }

    fun getApps() {
        _apps.value = if (_fragmentPos == 0) {
            App.sharedPrefs.firstPageApps ?: emptyList()
        } else {
            App.sharedPrefs.secondPageApps ?: emptyList()
        }

        _reorderedApps = _apps.value
    }

    fun saveStates() {
        if (_fragmentPos == 0) {
            App.sharedPrefs.firstPageApps = _reorderedApps
        } else {
            App.sharedPrefs.secondPageApps = _reorderedApps
        }
    }
}