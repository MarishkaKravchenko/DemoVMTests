package com.example.demovmtests.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseUseCase<IN_TYPE, OUT_TYPE>(coroutineContextProvider: CoroutineContextProvider) {
    private val job = Job()
    protected val uiScope = CoroutineScope(coroutineContextProvider.main + job)

    protected val isCallbackInitialised: Boolean
        get() = ::callback.isInitialized

    protected lateinit var callback: (OUT_TYPE) -> Unit

    abstract suspend fun execute(value: IN_TYPE, callback: (OUT_TYPE) -> Unit)

    open suspend fun executeCancelable(value: IN_TYPE, callerIdentifier: String, callback: (OUT_TYPE) -> Unit) {
        execute(value, callback)
    }

    open fun clear() {}

    open fun clearCancellable(callerIdentifier: String) {}
}
