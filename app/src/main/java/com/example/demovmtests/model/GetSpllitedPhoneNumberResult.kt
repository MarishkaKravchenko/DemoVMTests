package com.example.demovmtests.model

sealed class GetSpllitedPhoneNumberResult {
    data class Success(val phone: Phone) : GetSpllitedPhoneNumberResult()
    sealed class Error : GetSpllitedPhoneNumberResult() {
        object General : Error()
        object NoConnection : Error()
    }
}