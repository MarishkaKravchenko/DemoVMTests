package com.example.demovmtests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer

import com.example.demovmtests.base.UseCaseExecutor
import com.example.demovmtests.mapper.UserProfileDataToDomainMapper
import com.example.demovmtests.mapper.UserProfileDomainToUiMapper
import com.example.demovmtests.model.UpdateUserProfileResult
import com.example.demovmtests.model.UserProfile
import com.example.demovmtests.model.UserProfileForm
import com.example.demovmtests.model.UserProfileResult
import com.example.demovmtests.model.UserProfileUi
import com.example.demovmtests.usecase.GetUserProfileUseCase
import com.example.demovmtests.usecase.UpdateUserProfileDataUseCase
import com.example.demovmtests.util.DateFormatterVerbose
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date
import java.util.GregorianCalendar
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class UpdateUserProfileViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

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
        lifecycle = LifecycleRegistry(mock())
    }

    @Test
    fun `When onResume then state set to progress`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }

        // When
        cut.onResume()

        // Then
        assertEquals(UpdateUserProfileViewState.Progress, viewState)
    }

    @Test
    fun `Given fetched user profile successfully when onResume then state set to user profile view`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }

        val userProfile = mock<UserProfile>()
        val userProfileUi =
            UserProfileUi(
                "title", "firstName", "surname", "countryCode",
                "phoneNumher", Date(), "formattedDateOfBirth"
            )
        val useCaseResult = UserProfileResult.Success(userProfile)
        whenever(userProfileDomainToUiMapper.toUi(userProfile)).thenReturn(userProfileUi)

        // When
        cut.onResume()

        // Then
        val callbackCaptor = argumentCaptor<(UserProfileResult) -> Unit>()
        verify(useCaseExecutor).execute(eq(getUserProfileUseCase), callbackCaptor.capture())

        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(useCaseResult)

        // Then
        assertEquals(
            UpdateUserProfileViewState.Form(
                userProfileUi.title,
                userProfileUi.firstName,
                userProfileUi.surname,
                userProfileUi.countryCode,
                userProfileUi.phoneNumber,
                userProfileUi.dateOfBirth,
                userProfileUi.formattedDateOfBirth,
                showFirstNameError = false,
                showSurnameError = false,
                showPhoneNumberError = false,
                submitEnabled = false
            ), viewState
        )
    }

    @Test
    fun `Given error fetching user profile when onResume then state set to error`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }
        val useCaseResult = UserProfileResult.GeneralError

        // When
        cut.onResume()

        // Then
        val callbackCaptor = argumentCaptor<(UserProfileResult) -> Unit>()
        verify(useCaseExecutor).execute(eq(getUserProfileUseCase), callbackCaptor.capture())

        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(useCaseResult)

        // Then
        assertEquals(UpdateUserProfileViewState.GeneralError, viewState)
    }

    @Test
    fun `When onSaveButtonClick then state set to progress`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }

        val userProfileUi = mock<UserProfileUi>()

        // When
        cut.onSaveButtonClick(userProfileUi)

        // Then
        assertEquals(UpdateUserProfileViewState.Progress, viewState)
    }

    @Test
    fun `Given stored user profile successfully when onSaveButtonClick then state set to user profile view`() {
        // Given
        var navigationState: UpdateUserProfileNavigationEvent? = null
        observeNavigationState { navigationState = it }
        val userProfile = mock<UserProfile>()
        val userProfileUi =
            UserProfileUi(
                "title", "firstName", "surname", "countryCode",
                "phoneNumher", Date(), "formattedDateOfBirth"
            )
        val useCaseResult = UpdateUserProfileResult.SUCCESS
        whenever(userProfileDataToDomainMapper.toDomain(userProfileUi)).thenReturn(userProfile)

        // When
        cut.onSaveButtonClick(userProfileUi)

        // Then
        val callbackCaptor = argumentCaptor<(UpdateUserProfileResult) -> Unit>()
        verify(useCaseExecutor).execute(
            eq(updateUserProfileDataUseCase),
            eq(userProfile),
            callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(useCaseResult)

        // Then
        assertEquals(UpdateUserProfileNavigationEvent.BACK, navigationState)
    }

    @Test
    fun `Given error storing user profile when onSaveButtonClick then state set to error`() {
        // Given
        var viewState: UpdateUserProfileViewState? = null
        observeViewState { viewState = it }
        val useCaseResult = UpdateUserProfileResult.FAILURE
        val userProfile = mock<UserProfile>()
        val userProfileUi = mock<UserProfileUi>()
        whenever(userProfileDataToDomainMapper.toDomain(userProfileUi)).thenReturn(userProfile)

        // When
        cut.onSaveButtonClick(userProfileUi)

        // Then
        val callbackCaptor = argumentCaptor<(UpdateUserProfileResult) -> Unit>()
        verify(useCaseExecutor).execute(
            eq(updateUserProfileDataUseCase),
            eq(userProfile),
            callbackCaptor.capture()
        )

        val callback = callbackCaptor.firstValue

        // When
        callback.invoke(useCaseResult)

        // Then
        assertEquals(UpdateUserProfileViewState.SaveProfileError, viewState)
    }

    @Test
    fun `Given form isn't modified when onBack then goes back`() {
        // Given
        var navigationState: UpdateUserProfileNavigationEvent? = null
        observeNavigationState { navigationState = it }

        // When
        cut.onBackPressed()

        // Then
        assertEquals(UpdateUserProfileNavigationEvent.BACK, navigationState)
    }

    @Test
    fun `Given form modified when onBack then shows confirmation dialog`() {
        // Given
        var navigationState: UpdateUserProfileNavigationEvent? = null
        observeNavigationState { navigationState = it }
        val userData = UserProfileForm(
            "",
            "",
            "",
            "",
            "",
            GregorianCalendar(1999, 3, 1).time
        )
        given(dateFormatterVerbose.getFormattedDate(any(), any(), any())).willReturn("")

        // When
        cut.onFormDataChanged(userData)
        cut.onBackPressed()

        // Then
        assertEquals(UpdateUserProfileNavigationEvent.CONFIRMATION_DIALOG, navigationState)
    }

    @Test
    fun `When onDiscardClick then goes back`() {
        // Given
        var navigationState: UpdateUserProfileNavigationEvent? = null
        observeNavigationState { navigationState = it }

        // When
        cut.onDiscardClick()

        // Then
        assertEquals(UpdateUserProfileNavigationEvent.BACK, navigationState)
    }

    private fun observeViewState(observer: (UpdateUserProfileViewState) -> Unit) {
        Observer<UpdateUserProfileViewState>(observer).also { stateObserver ->
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            cut.observeViewState(LifecycleOwner { lifecycle }, stateObserver)
        }
    }

    private fun observeNavigationState(observer: (UpdateUserProfileNavigationEvent) -> Unit) {
        Observer<UpdateUserProfileNavigationEvent>(observer).also { navigationObserver ->
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            cut.observeNavigation(LifecycleOwner { lifecycle }, navigationObserver)
        }
    }
}