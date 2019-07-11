package com.example.demovmtests.repo

import android.util.Log
import com.example.demovmtests.extensions.isConnectionException
import com.example.demovmtests.model.GetSpllitedPhoneNumberResult
import com.example.demovmtests.model.Phone
import com.example.demovmtests.model.UpdateUserProfileResult
import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.model.UserProfileResult
import javax.inject.Inject

class AccountRepositoryImpl
@Inject constructor() : AccountRepository {

    override suspend fun splitPhoneNumber(phoneNumber: String): GetSpllitedPhoneNumberResult {
        return GetSpllitedPhoneNumberResult.Success(Phone("380", "555555555"))
    }

    override suspend fun getUserProfileData(): UserProfileResult {
        val userProfileResult: UserProfileResult

        try {
            val userProfile =
                UserProfile(
                    "",
                    "Mr",
                    "John",
                    "Doe",
                    "",
                    "555555555",
                    "1970-05-16"
                )
            userProfileResult = UserProfileResult.Success(userProfile)
        } catch (throwable: Throwable) {
            Log.e("Get user profile failed", throwable.message)
            return if (throwable.isConnectionException) {
                UserProfileResult.ConnectionError
            } else {
                UserProfileResult.GeneralError
            }
        }

        return userProfileResult
    }

    override suspend fun updateUserProfileData(
        userProfile: UserProfile
    ): UpdateUserProfileResult {
        return UpdateUserProfileResult.SUCCESS
    }
}