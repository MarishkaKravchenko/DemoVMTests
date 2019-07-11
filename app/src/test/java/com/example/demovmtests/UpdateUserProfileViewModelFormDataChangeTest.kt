package com.example.demovmtests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.example.demovmtests.base.UseCaseExecutor
import com.example.demovmtests.mapper.UserProfileDataToDomainMapper
import com.example.demovmtests.mapper.UserProfileDomainToUiMapper
import com.example.demovmtests.model.UserProfileForm
import com.example.demovmtests.usecase.GetUserProfileUseCase
import com.example.demovmtests.usecase.UpdateUserProfileDataUseCase
import com.example.demovmtests.util.DateFormatterVerbose
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import java.util.Arrays
import java.util.GregorianCalendar
import kotlin.test.assertEquals

private const val TITLE = "title"
private const val FORMATTED_DATE_OF_BIRTH = "dateOfBirth"
private val dateOfBirth = GregorianCalendar(1999, 3, 1).time

@RunWith(Parameterized::class)
class UpdateUserProfileViewModelFormDataChangeTest(
    private val firstName: String,
    private val surname: String,
    private val countryCode: String,
    private val phoneNumber: String,
    private val expectedForm: UpdateUserProfileViewState.Form
) {
    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<*>> {
            return Arrays.asList(
                arrayOf(
                    "", "", "", "", UpdateUserProfileViewState.Form(
                        TITLE,
                        "", "", "", "",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = true,
                        showSurnameError = true,
                        showPhoneNumberError = true,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "M", "", "", "", UpdateUserProfileViewState.Form(
                        TITLE,
                        "M", "", "", "",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = false,
                        showSurnameError = true,
                        showPhoneNumberError = true,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "", "M", "", "", UpdateUserProfileViewState.Form(
                        TITLE,
                        "", "M", "", "",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = true,
                        showSurnameError = false,
                        showPhoneNumberError = true,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "", "", "+44", "12345678", UpdateUserProfileViewState.Form(
                        TITLE,
                        "", "", "+44", "12345678",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = true,
                        showSurnameError = true,
                        showPhoneNumberError = false,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "M", "M", "", "", UpdateUserProfileViewState.Form(
                        TITLE,
                        "M", "M", "", "",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = false,
                        showSurnameError = false,
                        showPhoneNumberError = true,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "M", "", "+44", "12345678", UpdateUserProfileViewState.Form(
                        TITLE,
                        "M", "", "+44", "12345678",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = false,
                        showSurnameError = true,
                        showPhoneNumberError = false,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "", "M", "+44", "12345678", UpdateUserProfileViewState.Form(
                        TITLE,
                        "", "M", "+44", "12345678",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = true,
                        showSurnameError = false,
                        showPhoneNumberError = false,
                        submitEnabled = false
                    )
                ),
                arrayOf(
                    "M", "M", "+44", "12345678", UpdateUserProfileViewState.Form(
                        TITLE,
                        "M", "M", "+44", "12345678",
                        dateOfBirth,
                        FORMATTED_DATE_OF_BIRTH,
                        showFirstNameError = false,
                        showSurnameError = false,
                        showPhoneNumberError = false,
                        submitEnabled = true
                    )
                )
            )
        }
    }

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var cut: UpdateUserProfileViewModel

    @Mock
    lateinit var useCaseExecutor: UseCaseExecutor

    @Mock
    lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Mock
    lateinit var updateUserProfileDataUseCase: UpdateUserProfileDataUseCase

    @Mock
    lateinit var userProfileDataToDomainMapper: UserProfileDataToDomainMapper

    @Mock
    lateinit var userProfileDomainToUiMapper: UserProfileDomainToUiMapper

    @Mock
    lateinit var dateFormatterVerbose: DateFormatterVerbose

    private lateinit var lifecycle: LifecycleRegistry

    @Before
    fun setUp() {
        cut = UpdateUserProfileViewModel(
            useCaseExecutor,
            getUserProfileUseCase,
            updateUserProfileDataUseCase,
            userProfileDataToDomainMapper,
            userProfileDomainToUiMapper,
            dateFormatterVerbose
        )

        whenever(dateFormatterVerbose.getFormattedDate(1999, 4, 1)).thenReturn(FORMATTED_DATE_OF_BIRTH)
        lifecycle = LifecycleRegistry(mock())
    }

    @Test
    fun `test`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }
        val userProfileForm = UserProfileForm(TITLE, firstName, surname, countryCode, phoneNumber, dateOfBirth)

        // When
        cut.onFormDataChanged(userProfileForm)

        // Then
        assertEquals(expectedForm, viewState)
    }

    private fun observeViewState(observer: (UpdateUserProfileViewState) -> Unit) {
        Observer<UpdateUserProfileViewState>(observer).also { stateObserver ->
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            cut.observeViewState(LifecycleOwner { lifecycle }, stateObserver)
        }
    }
}