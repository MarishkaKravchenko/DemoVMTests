package com.example.demovmtests.di

import com.example.demovmtests.base.CoroutineContextProvider
import com.example.demovmtests.base.CoroutineContextProviderImpl
import dagger.Module
import dagger.Provides

import dagger.Reusable
import kotlinx.coroutines.Job

@Module
class BaseDomainModule {

    @Provides
    internal fun provideJob(): Job = Job()

    @Provides
    @Reusable
    fun provideCoroutineContextProvider(): CoroutineContextProvider = CoroutineContextProviderImpl()
}
