package com.example.demovmtests.repo

import com.example.demovmtests.model.GetSpllitedPhoneNumberResult
import com.example.demovmtests.model.UpdateUserProfileResult
import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.model.UserProfileResult

interface AccountRepository {
    suspend fun getUserProfileData(): UserProfileResult
    suspend fun splitPhoneNumber(phoneNumber: String): GetSpllitedPhoneNumberResult
    suspend fun updateUserProfileData(
        userProfile: UserProfile
    ): UpdateUserProfileResult
}