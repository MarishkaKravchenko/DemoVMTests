package com.example.demovmtests.util

private val CAPTURE_WHITESPACE_REGEX = "\\s+".toRegex()

fun String.toInitials() = split(' ')
    .mapNotNull { it.firstOrNull()?.toString() }
    .takeIf { it.isNotEmpty() }
    ?.reduce { accumulatedString, initialsElement -> accumulatedString + initialsElement }
    ?: ""

fun String.removeWhiteSpaces() = replace(CAPTURE_WHITESPACE_REGEX, "")