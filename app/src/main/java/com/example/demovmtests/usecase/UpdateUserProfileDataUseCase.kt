package com.example.demovmtests.usecase

import com.example.demovmtests.base.BackgroundExecuteUseCase
import com.example.demovmtests.base.CoroutineContextProvider
import com.example.demovmtests.model.UpdateUserProfileResult
import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.repo.AccountRepository
import dagger.Reusable
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@Reusable
class UpdateUserProfileDataUseCase
@Inject constructor(
    coroutineContextProvider: CoroutineContextProvider,
    private val accountRepository: AccountRepository
) : BackgroundExecuteUseCase<UserProfile, UpdateUserProfileResult>(coroutineContextProvider) {

    override suspend fun executeRequest(request: UserProfile, coroutineScope: CoroutineScope): UpdateUserProfileResult {
        return accountRepository.updateUserProfileData(request)
    }
}