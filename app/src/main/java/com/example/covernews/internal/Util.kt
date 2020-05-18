package com.example.covernews.internal

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

@RequiresApi(Build.VERSION_CODES.O)
fun setPublishedDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return current.format(formatter)
}


@RequiresApi(Build.VERSION_CODES.O)
fun setFromDate(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return current.format(formatter)
}

fun getCountry(): String {
    val local = Locale.getDefault()
    return local.country.toString().toLowerCase(Locale.ROOT)
}

fun getLanguage(): String{
    val locale = Locale.getDefault()
    val country = java.lang.String.valueOf(locale.language)
    return country.toLowerCase(Locale.ROOT)
}