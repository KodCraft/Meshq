package az.kodcraft.core.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Date
import java.util.Locale

fun getStartAndEndDateOfWeek(date: LocalDate): Pair<String, String> {
    val weekFields = WeekFields.of(Locale.getDefault())
    // Get the start date of the week based on the provided date
    val startDate = date.with(TemporalAdjusters.previousOrSame(weekFields.firstDayOfWeek))
    // Assuming the week is 7 days long, calculate the end date
    val endDate = startDate.plusDays(6)

    return startDate.toString() to endDate.toString()
}

fun localDateToTimestamp(date: LocalDate): Timestamp {
    val zonedDateTime = date.atStartOfDay(ZoneId.systemDefault())
    return Timestamp(zonedDateTime.toEpochSecond(), 0)
}

fun Date.formatDateToStringDatAndMonth(): String {
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault()) // Using US locale for English month names
    return formatter.format(this)
}