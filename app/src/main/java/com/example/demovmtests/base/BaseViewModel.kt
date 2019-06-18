package com.example.demovmtests.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<VIEW_STATE : ViewState, NAVIGATION : Navigation>
constructor(
    protected val useCaseExecutor: UseCaseExecutor
) : ViewModel() {

    internal var navigationState = SingleLiveEvent<NAVIGATION>()
    internal var viewState = MutableLiveData<VIEW_STATE>()

    protected val currentViewState: VIEW_STATE?
        get() = viewState.value

    fun observeViewState(owner: LifecycleOwner, observer: Observer<VIEW_STATE>) {
        viewState.observe(owner, observer)
    }

    fun removeViewState(observer: Observer<VIEW_STATE>) {
        viewState.removeObserver(observer)
    }

    fun observeNavigation(owner: LifecycleOwner, observer: Observer<NAVIGATION>) {
        navigationState.observe(owner, observer)
    }

    fun removeNavigation(observer: Observer<NAVIGATION>) {
        navigationState.removeObserver(observer)
    }

    fun updateState(newViewState: VIEW_STATE) {
        viewState.value = newViewState
    }

    fun navigateTo(navigation: NAVIGATION) {
        navigationState.value = navigation
    }

    protected fun <T> LiveData<T>.setValue(value: T) {
        if (this is MutableLiveData<T>) this.value = value
    }

    protected fun <T> LiveData<T>.postValue(value: T) {
        if (this is MutableLiveData<T>) this.postValue(value)
    }
}