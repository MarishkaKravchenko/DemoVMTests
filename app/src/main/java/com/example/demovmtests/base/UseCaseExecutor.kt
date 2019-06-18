package com.example.demovmtests.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class UseCaseExecutor
@Inject constructor(
    private val job: Job,
    coroutineContextProvider: CoroutineContextProvider
) {
    private val uiScope = CoroutineScope(coroutineContextProvider.main + job)
    private val ioScope = CoroutineScope(coroutineContextProvider.io + job)

    fun <OUT_TYPE> execute(useCase: BaseUseCase<Unit, OUT_TYPE>, callback: (OUT_TYPE) -> Unit = {}) =
        execute(useCase, Unit, callback)

    fun <IN_TYPE, OUT_TYPE> execute(
        useCase: BaseUseCase<IN_TYPE, OUT_TYPE>,
        value: IN_TYPE,
        callback: (OUT_TYPE) -> Unit = {}
    ) {
        uiScope.launch {
            useCase.execute(value, callback)
        }
    }

    fun <OUT_TYPE> executeCancellable(
        useCase: BaseUseCase<Unit, OUT_TYPE>,
        callerIdentifier: String,
        callback: (OUT_TYPE) -> Unit = {}
    ) {
        executeCancellable(useCase, Unit, callerIdentifier, callback)
    }

    fun <IN_TYPE, OUT_TYPE> executeCancellable(
        useCase: BaseUseCase<IN_TYPE, OUT_TYPE>,
        value: IN_TYPE,
        callerIdentifier: String,
        callback: (OUT_TYPE) -> Unit = {}
    ) {
        uiScope.launch {
            useCase.executeCancelable(value, callerIdentifier, callback)
        }
    }

    fun <IN_TYPE, OUT_TYPE> clear(useCase: BaseUseCase<IN_TYPE, OUT_TYPE>) {
        useCase.clear()
    }

    fun <IN_TYPE, OUT_TYPE> clearCancellable(useCase: BaseUseCase<IN_TYPE, OUT_TYPE>, callerIdentifier: String) {
        useCase.clearCancellable(callerIdentifier)
    }

    fun abortAll() {
        job.cancel()
    }
}