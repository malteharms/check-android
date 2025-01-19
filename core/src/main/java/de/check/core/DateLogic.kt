package de.check.core

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


fun getDateFromTimestamp(timestamp: Long): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
}

fun getTimestampFromDate(date: LocalDateTime): Long {
    return date.atZone(ZoneId.systemDefault()).toEpochSecond()
}
