package com.example.nap.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

//Creating and object instead of a class so we can use its methods anywhere without creating multiple instances
object DateFormatter {
    private val millisInHour = TimeUnit.HOURS.toMillis(1)
    private val millisInDays = TimeUnit.DAYS.toMillis(1)
    private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    fun formatCurrentDate(): String {
        return formatter.format(System.currentTimeMillis())
    }

    fun formatterDateToString(timeStamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeStamp

        return when {
            diff < millisInHour -> "Just Now"
            diff < millisInDays -> {
                val hours = TimeUnit.MICROSECONDS.toHours(diff)
                "$hours h ago"
            }

            else -> {
                formatter.format(timeStamp)
            }
        }
    }
}
