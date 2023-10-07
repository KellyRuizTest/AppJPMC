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

fun Int.ToTimeStringCurrent(timeZ: Int): Float {
    try {
        val offsetDate: OffsetDateTime = Instant.ofEpochSecond(this.toLong()).atOffset(ZoneOffset.ofTotalSeconds(timeZ))
        val outputDate = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
        val dateTime = outputDate.format(offsetDate)
        val hour = dateTime.substring(0, 2).toInt()
        var minute = dateTime.substring(3, 5).toFloat()
        minute /= if (minute < 10) 10 else 100
        return hour.toFloat() + minute
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toFloat()
}

fun Int.ToTimeFloatMinute(timeZ: Int): Int {
    try {
        val offsetDate: OffsetDateTime = Instant.ofEpochSecond(this.toLong()).atOffset(ZoneOffset.ofTotalSeconds(timeZ))
        val outputDate = DateTimeFormatter.ofPattern("mm", Locale.ENGLISH)
        return outputDate.format(offsetDate).toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toInt()
}
fun Int.ToTimeFloatHour(timeZ: Int): Int {
    try {
        val offsetDate: OffsetDateTime = Instant.ofEpochSecond(this.toLong()).atOffset(ZoneOffset.ofTotalSeconds(timeZ))
        val outputDate = DateTimeFormatter.ofPattern("HH", Locale.ENGLISH)

        return outputDate.format(offsetDate).toInt()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return this.toInt()
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
