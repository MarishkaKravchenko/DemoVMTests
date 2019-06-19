package com.example.demovmtests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.demovmtests.base.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class UpdateUserProfileFragment : Fragment() {

    private lateinit var viewModel: UpdateUserProfileViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_update_user_profile, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        setUpViews()
    }


    fun setUpViews() {
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(UpdateUserProfileViewModel::class.java)

//        countryCodeDropDown.setOnTextTouchListener { _, _ ->
//            showAreaCodePopup()
//            true
//        }
//
//        titleDropDown.setUpTitleDropDown { viewModel.onFormDataChanged(formDataProvider()) }
//
//        setUpChangeListeners()
//
//        dateOfBirthView.setUpDateDropDown(
//            object : DateProvider {
//                override fun getDayOfMonth() = dateOfBirth.get(Calendar.DAY_OF_MONTH)
//
//                override fun getMonth() = dateOfBirth.get(Calendar.MONTH)
//
//                override fun getYear() = dateOfBirth.get(Calendar.YEAR)
//            },
//            Calendar.getInstance().let {
//                it.add(Calendar.YEAR, -MINIMUM_AGE_YEARS)
//                it.timeInMillis
//            }
//        ) { year, month, dayOfMonth ->
//            dateOfBirth.set(Calendar.YEAR, year)
//            dateOfBirth.set(Calendar.MONTH, month)
//            dateOfBirth.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            viewModel.onFormDataChanged(formDataProvider())
//        }
//
//        setUpSaveButton()
    }


}