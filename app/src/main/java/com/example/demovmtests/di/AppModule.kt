package com.example.demovmtests.di

import android.app.Application
import android.content.Context

import dagger.Binds
import dagger.Module

import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    @Singleton
    internal abstract fun provideContext(application: Application): Context
}