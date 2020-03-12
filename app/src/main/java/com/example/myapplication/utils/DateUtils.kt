package com.example.myapplication.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object DateUtils {

    fun toTime(seconds: Long): String {
        val date = Date(seconds * 1000L)

        return SimpleDateFormat("HH:mm").format(date)
    }

    fun toDate(seconds: Long): String {
        val date = Date(seconds * 1000L)

        return SimpleDateFormat("dd, MMMM").format(date)
    }

    fun messageTime(seconds: Long): String {
        val date = Date(seconds * 1000L)

        return SimpleDateFormat(selectPattern(date)).format(date)
    }

    private fun selectPattern(date: Date): String{
        val currentDate = Date()

        return when (abs(currentDate.time - date.time) / 100) {
            in 0..SECONDS_IN_DAY -> "HH:mm"
            in SECONDS_IN_DAY + 1..SECONDS_IN_WEEK -> "E, dd"
            in SECONDS_IN_WEEK + 1..SECONDS_IN_YEAR -> "dd, MMMM"
            else -> "yyyy MMMM dd \n HH:mm"
        }
    }

    private val SECONDS_IN_DAY = TimeUnit.DAYS.toSeconds(1) //86400
    private val SECONDS_IN_WEEK= TimeUnit.DAYS.toSeconds(7)//(6.048 * 10f.pow(2))
    private val SECONDS_IN_YEAR= TimeUnit.DAYS.toSeconds(365)//(3.154 * 10f.pow(7))
}