package com.example.demovmtests

import com.example.demovmtests.base.BaseViewModel
import com.example.demovmtests.base.FormDataChangeCallback
import com.example.demovmtests.base.UseCaseExecutor
import com.example.demovmtests.mapper.UserProfileDataToDomainMapper
import com.example.demovmtests.mapper.UserProfileDomainToUiMapper
import com.example.demovmtests.model.UpdateUserProfileResult
import com.example.demovmtests.model.UserProfileForm
import com.example.demovmtests.model.UserProfileResult
import com.example.demovmtests.model.UserProfileUi
import com.example.demovmtests.usecase.GetUserProfileUseCase
import com.example.demovmtests.usecase.UpdateUserProfileDataUseCase
import com.example.demovmtests.util.DateFormatterVerbose
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateUserProfileViewModel
@Inject constructor(
    useCaseExecutor: UseCaseExecutor,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileDataUseCase: UpdateUserProfileDataUseCase,
    private val userProfileDataToDomainMapper: UserProfileDataToDomainMapper,
    private val userProfileDomainToUiMapper: UserProfileDomainToUiMapper,
    private val dateFormatterVerbose: DateFormatterVerbose
) : BaseViewModel<UpdateUserProfileViewState, UpdateUserProfileNavigationEvent>(useCaseExecutor),
    FormDataChangeCallback<UserProfileForm> {

    private val calendar by lazy { Calendar.getInstance() }

    private var isFormModified = false

    fun onResume() {
        updateState(UpdateUserProfileViewState.Progress)
        useCaseExecutor.execute(getUserProfileUseCase) { userProfileResult ->
            when (userProfileResult) {
                is UserProfileResult.Success -> {
                    val userProfileUi = userProfileDomainToUiMapper.toUi(userProfileResult.userProfile)
                    val formState = getFormState(
                        userProfileUi,
                        showFirstNameError = false,
                        showSurnameError = false,
                        showPhoneNumberError = false,
                        submitEnabled = false
                    )
                    updateState(formState)
                }
                is UserProfileResult.GeneralError -> updateState(UpdateUserProfileViewState.GeneralError)
                is UserProfileResult.ConnectionError -> updateState(UpdateUserProfileViewState.ConnectionError)
            }
        }
    }

    private fun getFormState(
        userProfileUi: UserProfileUi,
        showFirstNameError: Boolean,
        showSurnameError: Boolean,
        showPhoneNumberError: Boolean,
        submitEnabled: Boolean
    ): UpdateUserProfileViewState.Form {
        return UpdateUserProfileViewState.Form(
            userProfileUi.title,
            userProfileUi.firstName,
            userProfileUi.surname,
            userProfileUi.countryCode,
            userProfileUi.phoneNumber,
            userProfileUi.dateOfBirth,
            userProfileUi.formattedDateOfBirth,
            showFirstNameError,
            showSurnameError,
            showPhoneNumberError,
            submitEnabled
        )
    }

    fun onSaveButtonClick(userProfileUi: UserProfileUi) {
        updateState(UpdateUserProfileViewState.Progress)

        val userProfile = userProfileDataToDomainMapper.toDomain(userProfileUi)
        useCaseExecutor.execute(updateUserProfileDataUseCase, userProfile) { result ->
            when (result) {
                UpdateUserProfileResult.SUCCESS -> {
                    isFormModified = false
                    navigateTo(UpdateUserProfileNavigationEvent.BACK)
                }
                UpdateUserProfileResult.FAILURE -> updateState(UpdateUserProfileViewState.SaveProfileError)
            }
        }
    }

    override fun onFormDataChanged(data: UserProfileForm) {
        isFormModified = true
        val dateOfBirth = data.dateOfBirth
        calendar.time = dateOfBirth
        val formattedDateOfBirth =
            dateFormatterVerbose.getFormattedDate(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
            )

        val newState = UserProfileUi(
            data.title,
            data.firstName,
            data.surname,
            data.countryCode,
            data.phoneNumber,
            dateOfBirth,
            formattedDateOfBirth
        )
        val firstNameValid = newState.firstName.isNotEmpty()
        val surnameValid = newState.surname.isNotEmpty()
        val phoneNumberValid = newState.phoneNumber.length in 8..10
        val formValid = firstNameValid && surnameValid && phoneNumberValid
        updateState(getFormState(newState, !firstNameValid, !surnameValid, !phoneNumberValid, formValid))
    }

    fun onDiscardClick() {
        isFormModified = false
        navigateTo(UpdateUserProfileNavigationEvent.BACK)
    }

    fun onBackPressed(): Boolean {
        val navigationEvent = if (isFormModified) {
            UpdateUserProfileNavigationEvent.CONFIRMATION_DIALOG
        } else {
            UpdateUserProfileNavigationEvent.BACK
        }
        navigateTo(navigationEvent)
        return true
    }
}