package com.example.demovmtests.base

import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {

    val main: CoroutineContext
    val io: CoroutineContext
}