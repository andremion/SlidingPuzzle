package io.github.andremion.slidingpuzzle

import android.app.Application
import io.github.andremion.slidingpuzzle.di.initDI
import org.koin.android.ext.koin.androidContext

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDI().androidContext(this)
    }
}
