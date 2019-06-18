package com.example.demovmtests.util

import dagger.Reusable
import javax.inject.Inject

@Reusable
class DateFormatterVerbose
@Inject constructor() : DateFormatter() {
    override val dateFormat = "d MMM yyyy"
}