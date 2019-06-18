package com.example.demovmtests.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BackgroundExecuteUseCase<REQUEST, RESPONSE> constructor(
    private val coroutineContextProvider: CoroutineContextProvider
) : BaseUseCase<REQUEST, RESPONSE>(coroutineContextProvider) {

    protected val jobs = HashMap<String, Job>()

    override suspend fun execute(
        value: REQUEST,
        callback: (RESPONSE) -> Unit
    ) {
        this.callback = callback

        uiScope.launch {
            val result = withContext(coroutineContextProvider.io) {
                executeRequest(value, coroutineScope = this)
            }

            result.let(callback)
        }
    }

    override suspend fun executeCancelable(value: REQUEST, callerIdentifier: String, callback: (RESPONSE) -> Unit) {
        this.callback = callback

        jobs[callerIdentifier]?.cancel()

        val newJob = uiScope.launch {
            val result = withContext(coroutineContextProvider.io) {
                executeRequest(value, coroutineScope = this)
            }

            result.let(callback)
        }
        jobs[callerIdentifier] = newJob
    }

    override fun clearCancellable(callerIdentifier: String) {
        jobs[callerIdentifier]?.let { jobToCancel ->
            jobToCancel.cancel()
            jobs.remove(callerIdentifier)
        }
    }

    abstract suspend fun executeRequest(
        request: REQUEST,
        coroutineScope: CoroutineScope
    ): RESPONSE
}