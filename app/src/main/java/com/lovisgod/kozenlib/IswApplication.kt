package com.lovisgod.kozenlib

import android.app.Application
import android.content.ContextWrapper
import com.lovisgod.kozenlib.di.ExportModules
import com.pixplicity.easyprefs.library.Prefs
import org.koin.core.context.GlobalContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


object IswApplication {



     fun onCreate(app: Application) {

        loadModules(app)

        Prefs.Builder()
            .setContext(app)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName("com.lovisgod.kozenlib")
            .setUseDefaultSharedPreference(true)
            .build()
    }



    fun appContext(app: Application) = module(override = true) {
        single { app.applicationContext }

    }

    fun loadModules(app: Application){
// set up koin
        var modules = arrayListOf<Module>()
        modules.add(appContext(app))
        modules.addAll(ExportModules.modules)

        startKoin {
            modules(modules)
        }
    }
}

class SampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        IswApplication.onCreate(this)
    }
}