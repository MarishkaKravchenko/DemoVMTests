package com.example.demovmtests

import com.example.demovmtests.base.ViewState
import java.util.Date

sealed class UpdateUserProfileViewState : ViewState {
    data class Form(
        val title: String,
        val firstName: String,
        val surname: String,
        val countryCode: String,
        val phoneNumber: String,
        val dateOfBirth: Date,
        val formattedDateOfBirth: String,
        val showFirstNameError: Boolean,
        val showSurnameError: Boolean,
        val showPhoneNumberError: Boolean,
        val submitEnabled: Boolean
    ) : UpdateUserProfileViewState()

    object Progress : UpdateUserProfileViewState()

    object SaveProfileError : UpdateUserProfileViewState()

    object GeneralError : UpdateUserProfileViewState()

    object ConnectionError : UpdateUserProfileViewState()
}