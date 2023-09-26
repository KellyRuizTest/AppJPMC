package com.loder.weatherappjpmc.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Int.ToDateTimeString(): String {
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

fun Int.ToTimeString(): String {
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

fun Int.ToTimeStringAux(): String {
    var sdf = SimpleDateFormat("MMM dd")
    var dateTime = Date(this * 1000L)
    return sdf.format(dateTime)
}

fun Double.kelvinToCelsius(): Int {
    return (this - 273.15).toInt()
}
