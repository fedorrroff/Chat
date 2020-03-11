package com.example.myapplication.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.pow

object DateUtils {

    fun toDate(seconds: Long): String {
        val date = Date(seconds * 1000L)

        return SimpleDateFormat(selectPattern(date)).format(date)
    }

    private fun selectPattern(date: Date): String{
        val currentDate = Date()

        val differ = abs(currentDate.time - date.time)
        return when (differ) {
            in 0..SECONDS_IN_DAY -> "HH:mm"
            in SECONDS_IN_DAY + 1..SECONDS_IN_WEEK -> "E, dd"
            in SECONDS_IN_WEEK + 1..SECONDS_IN_YEAR -> "dd, MMMM"
            else -> "yyyy MMMM dd \n HH:mm"

        }
    }

    private val SECONDS_IN_DAY =TimeUnit.DAYS.toSeconds(1) //86400
    private val SECONDS_IN_WEEK=TimeUnit.DAYS.toSeconds(7)//; (6.048 * 10f.pow(2)).toInt()
    private val SECONDS_IN_YEAR = TimeUnit.DAYS.toSeconds(365)//(3.154 * 10f.pow(7)).toInt()
}