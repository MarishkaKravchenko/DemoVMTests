package com.example.demovmtests.model

data class UserProfile(
    val userId: String = "",
    val title: String = "",
    val firstName: String,
    val lastName: String,
    var countryCode: String? = "",
    var mobile: String,
    val dateOfBirth: String
)