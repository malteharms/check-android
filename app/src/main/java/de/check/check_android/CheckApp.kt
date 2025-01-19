package de.check.check_android

import android.app.Application
import de.check.check_android.di.AppModule
import de.check.check_android.di.AppModuleImpl

class CheckApp: Application() {

    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(context = this)
    }

}