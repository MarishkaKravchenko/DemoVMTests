package com.example.demovmtests.base

interface FormDataChangeCallback<in DATA_TYPE> {
    fun onFormDataChanged(data: DATA_TYPE)
}