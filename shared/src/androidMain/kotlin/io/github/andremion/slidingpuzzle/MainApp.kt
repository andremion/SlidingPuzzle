package io.github.andremion.slidingpuzzle

import android.app.Application
import io.github.andremion.slidingpuzzle.di.initDI
import org.koin.android.ext.koin.androidContext

class MainApp : Application() {
    companion object {
        lateinit var INSTANCE: MainApp
            private set
    }

    override fun onCreate() {
        INSTANCE = this
        super.onCreate()

        initDI().androidContext(this)
    }
}
