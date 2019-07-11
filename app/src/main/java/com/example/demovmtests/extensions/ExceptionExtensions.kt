package com.example.demovmtests.extensions

import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

val Throwable.isConnectionException: Boolean
    get() = this is UnknownHostException ||
            this is NoRouteToHostException ||
            this is SocketTimeoutException ||
            this.cause?.isConnectionException ?: false

val Throwable.isConnectionTimeoutException: Boolean
    get() = this is ConnectException ||
            this is SocketTimeoutException ||
            this.cause?.isConnectionException ?: false