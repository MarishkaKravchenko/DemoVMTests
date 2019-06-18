package com.example.demovmtests.model

sealed class UserProfileResult {
    data class Success(val userProfile: UserProfile) : UserProfileResult()
    object ConnectionError : UserProfileResult()
    object GeneralError : UserProfileResult()
}