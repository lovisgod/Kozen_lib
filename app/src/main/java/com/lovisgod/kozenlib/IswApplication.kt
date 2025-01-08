package com.lovisgod.kozenlib

import android.app.Application
import android.content.ContextWrapper
import com.lovisgod.kozenlib.di.DependencyContainer
import com.pixplicity.easyprefs.library.Prefs


object IswApplication {



     fun onCreate(app: Application) {

        Prefs.Builder()
            .setContext(app)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName("com.lovisgod.kozenlib")
            .setUseDefaultSharedPreference(true)
            .build()

         DependencyContainer.init(app)
    }
}

class SampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        IswApplication.onCreate(this)
    }
}