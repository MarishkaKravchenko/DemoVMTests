package com.example.demovmtests.mapper

import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.model.UserProfileUi
import com.example.demovmtests.util.DateFormatterShort
import com.example.demovmtests.util.DateFormatterVerbose
import dagger.Reusable
import java.util.Calendar
import javax.inject.Inject

@Reusable
class UserProfileDomainToUiMapper
@Inject constructor(
    private var dateFormatterShort: DateFormatterShort,
    private var dateFormatterVerbose: DateFormatterVerbose
) {
    private val calendar by lazy { Calendar.getInstance() }

    fun toUi(userProfile: UserProfile): UserProfileUi {
        val dateOfBirth = dateFormatterShort.getDate(userProfile.dateOfBirth)
        val formattedDateOfBirth = if (dateOfBirth != null) {
            calendar.time = dateOfBirth
            dateFormatterVerbose.getFormattedDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            calendar.timeInMillis = 0
            ""
        }

        return UserProfileUi(
            userProfile.title,
            userProfile.firstName,
            userProfile.lastName,
            "+${userProfile.countryCode}",
            userProfile.mobile,
            calendar.time,
            formattedDateOfBirth
        )
    }
}