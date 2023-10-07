package com.loder.weatherappjpmc.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Int.ToDateTimeString(timeZ: Int): String {
    try {
        val offsetDate: OffsetDateTime = Instant.ofEpochSecond(this.toLong()).atOffset(ZoneOffset.ofTotalSeconds(timeZ))
        val outputDate = DateTimeFormatter.ofPattern("dd MMM, yyyy - hh:mm a", Locale.ENGLISH)
        return outputDate.format(offsetDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.ToTimeString(timeZ: Int): String {
    try {
        val offsetDate: OffsetDateTime = Instant.ofEpochSecond(this.toLong()).atOffset(ZoneOffset.ofTotalSeconds(timeZ))
        val outputDate = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
        return outputDate.format(offsetDate)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.ToTimeStringCurrent(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.ToTimeStringAux(): String {
    var sdf = SimpleDateFormat("MMM dd", Locale.ENGLISH)
    var dateTime = Date(this * 1000L)
    return sdf.format(dateTime)
}

fun Int.ToDateTimeStringCurrent(): String {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd MMM, yyyy - hh:mm a", Locale.ENGLISH)

        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toString()
}

fun Int.ToTimeStringInt(): Int {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time).toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this
}

fun Int.ToTimeFloatMinute(): Int {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("mm", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        val hour = outputDateFormat.format(calendar.time).toInt()

        return hour
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toInt()
}
fun Int.ToTimeFloatHour(): Int {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("HH", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        val hour = outputDateFormat.format(calendar.time).toInt()

        return hour
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toInt()
}

fun Int.ToDateDay(): Int {
    try {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this * 1000.toLong()

        val outputDateFormat = SimpleDateFormat("dd", Locale.ENGLISH)
        outputDateFormat.timeZone = TimeZone.getDefault()
        return outputDateFormat.format(calendar.time).toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this
}

fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}
