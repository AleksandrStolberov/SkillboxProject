package com.example.skillcinema

import android.app.Application
import com.example.skillcinema.data.dataSource.database.Database
import com.example.skillcinema.data.model.SearchSettings
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        searchSettings = SearchSettings(Calendar.getInstance().get(Calendar.YEAR))
        Database.init(this)
    }

    companion object {
        lateinit var searchSettings: SearchSettings
    }

}