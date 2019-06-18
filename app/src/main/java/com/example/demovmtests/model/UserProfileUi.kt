package com.example.demovmtests.model

import java.util.Date

data class UserProfileUi(
    val title: String,
    val firstName: String,
    val surname: String,
    val countryCode: String,
    val phoneNumber: String,
    val dateOfBirth: Date,
    val formattedDateOfBirth: String
)