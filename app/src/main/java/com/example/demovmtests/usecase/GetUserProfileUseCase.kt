package com.example.demovmtests.usecase

import com.example.demovmtests.base.BackgroundExecuteUseCase
import com.example.demovmtests.base.CoroutineContextProvider
import com.example.demovmtests.model.GetSpllitedPhoneNumberResult
import com.example.demovmtests.model.UserProfileResult
import com.example.demovmtests.repo.AccountRepository
import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@Reusable
class GetUserProfileUseCase
@Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val accountRepository: AccountRepository
    ) : BackgroundExecuteUseCase<Unit, UserProfileResult>(coroutineContextProvider) {

    override suspend fun executeRequest(request: Unit, coroutineScope: CoroutineScope): UserProfileResult {
        val userProfileResult = accountRepository.getUserProfileData()
        return when (userProfileResult) {
            is UserProfileResult.Success -> {
                splitPhoneNumber(userProfileResult)
            }
            is UserProfileResult.ConnectionError -> UserProfileResult.ConnectionError
            is UserProfileResult.GeneralError -> UserProfileResult.GeneralError
        }
    }

    private suspend fun splitPhoneNumber(userProfileResult: UserProfileResult.Success): UserProfileResult {
        val splitPhoneNumberResult = accountRepository.splitPhoneNumber(userProfileResult.userProfile.mobile)
        return when (splitPhoneNumberResult) {
            is GetSpllitedPhoneNumberResult.Success -> {
                userProfileResult.apply {
                    userProfile.countryCode = splitPhoneNumberResult.phone.countryCode
                    userProfile.mobile = splitPhoneNumberResult.phone.mobileNumber
                }
            }

            is GetSpllitedPhoneNumberResult.Error.General -> UserProfileResult.GeneralError
            is GetSpllitedPhoneNumberResult.Error.NoConnection -> UserProfileResult.ConnectionError
        }
    }
}