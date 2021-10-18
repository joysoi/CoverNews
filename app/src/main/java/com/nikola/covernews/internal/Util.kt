package com.nikola.covernews.internal


import android.util.Log
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.String.valueOf
import java.text.ParseException
import java.text.SimpleDateFormat

import java.util.*

fun dateToTime(oldString: String?): String? {
    var isTime: String? = null
    val prettyTime = PrettyTime(Locale.getDefault())

    try {
        val sdf = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            Locale.ENGLISH
        )
        val date: Date = sdf.parse(oldString)
        isTime = prettyTime.format(date)
    } catch (e: ParseException) {
        Log.e("DateToTime", "Parsing error!", e)
    }
    return isTime
}

fun setPublishedDate(): String{
    val current = ZonedDateTime.now()
    return current.format(DateTimeFormatter.ISO_DATE)
}

fun setPeriodDate(): String{
    val current = ZonedDateTime.now()
    return current.format(DateTimeFormatter.ISO_DATE)
}

fun getCountry(): String {
    val local = Locale.getDefault()
    return local.country.toString().lowercase(Locale.ROOT)
}

fun getLanguage(): String{
    val locale = Locale.getDefault()
    val country = valueOf(locale.language)
    return country.lowercase(Locale.ROOT)
}