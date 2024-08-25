package com.lovisgod.kozenlib

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.ContextWrapper
import com.lovisgod.kozenlib.di.ExportModules
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext

import com.pixplicity.easyprefs.library.Prefs




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
        StandAloneContext.loadKoinModules(modules)
    }
}

class SampleApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        IswApplication.onCreate(this)
    }
}