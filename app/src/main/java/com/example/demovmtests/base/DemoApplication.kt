package com.example.demovmtests.base

import android.app.Application
import com.example.demovmtests.di.AppComponent
import com.example.demovmtests.di.DaggerAppComponent

open class DemoApplication : Application() {

    open var appComponent: AppComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }
}