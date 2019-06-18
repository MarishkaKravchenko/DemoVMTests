package com.example.demovmtests.mapper

import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.model.UserProfileUi
import com.example.demovmtests.util.DateFormatterShort
import com.example.demovmtests.util.DateFormatterVerbose
import com.example.demovmtests.util.removeWhiteSpaces
import dagger.Reusable
import java.util.Calendar
import javax.inject.Inject

@Reusable
class UserProfileDataToDomainMapper
@Inject constructor(
    private val dateFormatterShort: DateFormatterShort,
    private val dateFormatterVerbose: DateFormatterVerbose
) {
    private val calendar by lazy { Calendar.getInstance() }

    fun toDomain(userProfileUi: UserProfileUi): UserProfile {
        val dateOfBirthDate = dateFormatterVerbose.getDate(userProfileUi.formattedDateOfBirth)
        calendar.time = dateOfBirthDate

        return UserProfile(
            "",
            userProfileUi.title,
            userProfileUi.firstName,
            userProfileUi.surname,
            userProfileUi.countryCode.removeWhiteSpaces(),
            (userProfileUi.countryCode + userProfileUi.phoneNumber).removeWhiteSpaces(),
            dateFormatterShort.getFormattedDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
    }
}
