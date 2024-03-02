package com.ekorahy.gitus.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ekorahy.gitus.data.datastore.SettingPreferences
import com.ekorahy.gitus.view.main.MainViewModel

class ViewModelFactoryDataStore(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel clsss: " + modelClass.name)
    }
}