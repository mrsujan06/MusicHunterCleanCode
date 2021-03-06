package com.zero.musichunter

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.example.data.di.components.DaggerDataComponent
import com.example.data.di.components.DataComponent
import com.zero.musichunter.di.components.ApplicationComponent
import com.zero.musichunter.di.components.DaggerApplicationComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    @set:VisibleForTesting
    lateinit var appComponent: ApplicationComponent

    @VisibleForTesting
    val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(baseContext) }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    private fun initDagger(): ApplicationComponent =
        DaggerApplicationComponent.factory().create(this, dataComponent)

}