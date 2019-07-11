package com.example.demovmtests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demovmtests.base.HasProgressView
import com.example.demovmtests.base.HasStatusView
import com.example.demovmtests.base.ViewModelFactory
import com.example.demovmtests.extensions.DateProvider
import com.example.demovmtests.extensions.setUpDateDropDown
import com.example.demovmtests.extensions.setUpTitleDropDown
import com.example.demovmtests.extensions.toGone
import com.example.demovmtests.model.UserProfileForm
import com.example.demovmtests.model.UserProfileUi
import com.example.demovmtests.util.FormDataChangeTextWatcher
import com.example.demovmtests.util.KeyboardController
import com.example.demovmtests.util.SimpleDialogProvider
import com.example.demovmtests.util.StatusView
import com.example.demovmtests.util.ValidatorTextWatcher
import com.example.demovmtests.widget.TextInputView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_country_code_drop_down as countryCodeDropDown
import java.util.Calendar
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_date_of_birth as dateOfBirthView
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_first_name as firstNameEdit
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_phone_number as phoneNumberEdit
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_save_changes_button as saveButton
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_surname as surnameEdit
import kotlinx.android.synthetic.main.fragment_update_user_profile.update_user_profile_title_dropdown as titleDropDown

private const val MINIMUM_AGE_YEARS = 10

class UpdateUserProfileFragment : Fragment() {


    private val statusView: StatusView by lazy {
        val hasStatusView = activity as HasStatusView
        hasStatusView.statusView
    }

    @Inject
    lateinit var simpleDialogProvider: SimpleDialogProvider
    @Inject
    lateinit var keyboardController: KeyboardController

    private val activityWithProgressView by lazy { requireActivity() as HasProgressView }

    private val popupMenu: PopupMenu by lazy {
        createPopupMenu()
    }

    private val dateOfBirth: Calendar by lazy { Calendar.getInstance() }

    private lateinit var viewModel: UpdateUserProfileViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val updateUserProfileStateObserver by lazy {
        Observer<UpdateUserProfileViewState> { state -> updateViewByState(state) }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_update_user_profile, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        viewModel.observeViewState(this, updateUserProfileStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeViewState(updateUserProfileStateObserver)
    }

    private val formDataProvider = {
        UserProfileForm(
            title = titleDropDown.text.toString(),
            firstName = firstNameEdit.text.toString(),
            surname = surnameEdit.text.toString(),
            countryCode = countryCodeDropDown.text.toString(),
            phoneNumber = phoneNumberEdit.text.toString(),
            dateOfBirth = dateOfBirth.time
        )
    }

    private val firstNameTextWatcher by lazy {
        ValidatorTextWatcher(
            viewModel,
            firstNameEdit.inputView,
            formDataProvider
        )
    }
    private val surnameTextWatcher by lazy {
        ValidatorTextWatcher(
            viewModel,
            surnameEdit.inputView,
            formDataProvider
        )
    }
    private val phoneNumberTextWatcher by lazy {
        FormDataChangeTextWatcher(
            viewModel,
            formDataProvider
        )
    }


    fun setUpViews() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(UpdateUserProfileViewModel::class.java)

        countryCodeDropDown.setFocusableInput(false)
        countryCodeDropDown.setOnTextTouchListener { _, _ ->
            keyboardController.hideKeyboard(requireActivity())
            showAreaCodePopup()
            true
        }

        titleDropDown.setUpTitleDropDown { viewModel.onFormDataChanged(formDataProvider()) }

        setUpChangeListeners()

        dateOfBirthView.setUpDateDropDown(
            object : DateProvider {
                override fun getDayOfMonth() = dateOfBirth.get(Calendar.DAY_OF_MONTH)

                override fun getMonth() = dateOfBirth.get(Calendar.MONTH)

                override fun getYear() = dateOfBirth.get(Calendar.YEAR)
            },
            Calendar.getInstance().let {
                it.add(Calendar.YEAR, -MINIMUM_AGE_YEARS)
                it.timeInMillis
            }
        ) { year, month, dayOfMonth ->
            dateOfBirth.set(Calendar.YEAR, year)
            dateOfBirth.set(Calendar.MONTH, month)
            dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            viewModel.onFormDataChanged(formDataProvider())
        }

        setUpSaveButton()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onResume()
    }

    private fun showAreaCodePopup() {
        popupMenu.show()
    }

    private fun createPopupMenu(): PopupMenu {
        val popup = PopupMenu(requireContext(), countryCodeDropDown.inputView)
        val areaCodes = resources.getStringArray(R.array.area_codes)
        val areaCodeCountries = resources.getStringArray(R.array.area_code_countries)
        if (areaCodes.size != areaCodeCountries.size) {
            throw IllegalArgumentException(
                "Area codes and Area code countries should have same size. " +
                        "Current: areaCodes.size = ${areaCodes.size} areaCodeCountries.size = ${areaCodeCountries.size}"
            )
        }
        for ((i, areaCode) in areaCodes.withIndex()) {
            val formattedAreaCode = "${areaCodeCountries[i]} ($areaCode)"
            popup.menu.add(Menu.NONE, Menu.NONE, i, formattedAreaCode)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            countryCodeDropDown.text = areaCodes[menuItem.order]
            viewModel.onFormDataChanged(formDataProvider())
            true
        }

        return popup
    }

    private fun setUpChangeListeners() {
        firstNameEdit.addTextChangedListener(firstNameTextWatcher)
        surnameEdit.addTextChangedListener(surnameTextWatcher)
        phoneNumberEdit.addTextChangedListener(phoneNumberTextWatcher)
    }

    private fun setUpSaveButton() {
        saveButton.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                keyboardController.hideKeyboard(requireActivity())
            }
        }
        saveButton.setOnClickListener {
            keyboardController.hideKeyboard(requireActivity())
            viewModel.onSaveButtonClick(
                UserProfileUi(
                    title = titleDropDown.text.toString(),
                    firstName = firstNameEdit.text.toString(),
                    surname = surnameEdit.text.toString(),
                    countryCode = countryCodeDropDown.text.toString(),
                    phoneNumber = phoneNumberEdit.text.toString(),
                    dateOfBirth = dateOfBirth.time,
                    formattedDateOfBirth = dateOfBirthView.text.toString()
                )
            )
        }
    }

    private fun updateViewByState(viewState: UpdateUserProfileViewState) {
        activityWithProgressView.hideProgress()
        when (viewState) {
            is UpdateUserProfileViewState.Progress -> activityWithProgressView.showProgress()
            is UpdateUserProfileViewState.Form -> updateViews(viewState)
            is UpdateUserProfileViewState.SaveProfileError -> {
                simpleDialogProvider.showInfoDialog(
                    requireActivity(),
                    R.string.error_update_user_profile_failed_title,
                    R.string.error_update_user_profile_failed_body
                )
            }
            is UpdateUserProfileViewState.ConnectionError -> {
                statusView.showConnectionErrorStatus { statusView.toGone() }
            }
            is UpdateUserProfileViewState.GeneralError -> {
                statusView.showConnectionErrorStatus { statusView.toGone() }
            }
        }
    }

    private fun updateViews(viewState: UpdateUserProfileViewState.Form) {
        titleDropDown.text = viewState.title
        countryCodeDropDown.text = viewState.countryCode

        disableChangeListeners()
        updateIfChanged(firstNameEdit, viewState.firstName)
        updateIfChanged(surnameEdit, viewState.surname)
        updateIfChanged(phoneNumberEdit, viewState.phoneNumber)
        enableChangeListeners()

        firstNameEdit.error =
            getErrorMessageIfNeeded(viewState.showFirstNameError, R.string.error_invalid_first_name)
        surnameEdit.error =
            getErrorMessageIfNeeded(viewState.showSurnameError, R.string.error_invalid_surname)
        phoneNumberEdit.error =
            getErrorMessageIfNeeded(
                viewState.showPhoneNumberError,
                R.string.error_invalid_phone_number
            )

        dateOfBirthView.text = viewState.formattedDateOfBirth
        dateOfBirth.time = viewState.dateOfBirth
        saveButton.isEnabled = viewState.submitEnabled
    }

    private fun getErrorMessageIfNeeded(
        shouldShow: Boolean,
        @StringRes errorMessageId: Int
    ) = if (shouldShow) {
        getString(errorMessageId)
    } else {
        ""
    }

    private fun enableChangeListeners() {
        firstNameTextWatcher.isEnabled = true
        surnameTextWatcher.isEnabled = true
        phoneNumberTextWatcher.isEnabled = true
    }

    private fun disableChangeListeners() {
        firstNameTextWatcher.isEnabled = false
        surnameTextWatcher.isEnabled = false
        phoneNumberTextWatcher.isEnabled = false
    }

    private fun updateIfChanged(textInputView: TextInputView, text: String) {
        if (textInputView.text.toString() != text) {
            textInputView.text = text
        }
    }
}