package com.example.demovmtests.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.demovmtests.MainActivity
import com.example.demovmtests.UpdateUserProfileFragment
import com.example.demovmtests.UpdateUserProfileViewModel
import com.example.demovmtests.base.ViewModelFactory
import com.example.demovmtests.base.ViewModelFactoryImpl
import com.example.demovmtests.base.ViewModelKey
import com.example.demovmtests.repo.AccountRepository
import com.example.demovmtests.repo.AccountRepositoryImpl

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    @Singleton
    internal abstract fun provideContext(application: Application): Context

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactoryImpl): ViewModelFactory

    @Binds
    @IntoMap
    @ViewModelKey(UpdateUserProfileViewModel::class)
    abstract fun bindUpdateUserProfileViewModel(viewModel: UpdateUserProfileViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeUpdateUserProfileFragmentInjector(): UpdateUserProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @Binds
    @Reusable
    abstract fun bindAccountRepositoryImpl(accountRepositoryImpl: AccountRepositoryImpl): AccountRepository
}