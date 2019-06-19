package com.example.demovmtests.base

import android.app.Activity
import android.app.Application
import com.example.demovmtests.di.AppComponent
import com.example.demovmtests.di.DaggerAppComponent
import com.example.demovmtests.usecase.GetUserProfileUseCase
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

open class DemoApplication : Application(), HasActivityInjector {

    @Inject
    internal lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    internal  lateinit var case : GetUserProfileUseCase

    open var appComponent: AppComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> =
        activityDispatchingAndroidInjector
}