package com.marco.poccropimagewithgesture

import android.app.Application
import com.marco.poccropimagewithgesture.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@Application)
            modules(
                presentationModule
            )
        }
    }
}