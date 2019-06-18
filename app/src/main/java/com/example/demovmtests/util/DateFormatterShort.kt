package com.example.demovmtests.util

import dagger.Reusable
import javax.inject.Inject

@Reusable
class DateFormatterShort
@Inject constructor() : DateFormatter() {
    override val dateFormat = "yyyy-MM-dd"
}