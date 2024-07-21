package az.kodcraft.core.presentation.composable.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarEvent
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarIntent
import az.kodcraft.core.presentation.composable.calendar.contract.CalendarUiState
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.Gray100
import az.kodcraft.core.presentation.theme.HighlightBlue
import az.kodcraft.core.utils.noRippleClickable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

@Composable
fun MonthlyCalendar(
    viewModel: CalendarViewModel = hiltViewModel(),
    onDateClick: (LocalDate) -> Unit = {},
    onMonthChanged: (YearMonth) -> Unit = {},
    labels: List<LocalDate> = emptyList(),
    selectedDate: LocalDate
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is CalendarEvent.MonthChanged -> onMonthChanged(it.yearMonth)
            }
        }
    }
    Calendar(
        uiState = uiState,
        onDateClick = onDateClick,
        labels = labels,
        onPreviousMonthButtonClicked = { viewModel.acceptIntent(CalendarIntent.PreviousMonthClicked) },
        onNextMonthButtonClicked = { viewModel.acceptIntent(CalendarIntent.NextMonthClicked) },
        selectedDate = selectedDate
    )
}

@Composable
fun Calendar(
    uiState: CalendarUiState,
    onPreviousMonthButtonClicked: () -> Unit = {},
    onNextMonthButtonClicked: () -> Unit = {},
    onDateClick: (LocalDate) -> Unit = {},
    labels: List<LocalDate> = emptyList(),
    selectedDate: LocalDate
) {
    Column {
        Header(
            yearMonth = uiState.yearMonth,
            onPreviousMonthButtonClicked = onPreviousMonthButtonClicked,
            onNextMonthButtonClicked = onNextMonthButtonClicked
        )
        Spacer(modifier = Modifier.height(6.dp))
        Content(
            uiState.dates,
            onDateClickListener = onDateClick,
            labels = labels.filter { YearMonth.from(it) == uiState.yearMonth },
            selectedDate = selectedDate
        )
    }
}

@Composable
fun Content(
    dates: List<CalendarUiState.Date>,
    onDateClickListener: (LocalDate) -> Unit,
    labels: List<LocalDate> = emptyList(),
    selectedDate: LocalDate
) {
    // Calculate the start and end dates of the week containing the selected date
    val startOfWeek = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))


    Column {
        Row {
            DayOfWeek.entries.forEach { day ->
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(2),
                    style = MaterialTheme.typography.bodyMedium.copy(color = if (day == DayOfWeek.SUNDAY || day == DayOfWeek.SATURDAY) HighlightBlue else Gray100),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
        var index = 0
        repeat(6) {

            if (index >= dates.size) return@repeat
            // Check if the current week is the selected week
            val isSelectedWeek = dates.subList(index, (index + 7).coerceAtMost(dates.size)).any {
                val date = it.localDate
                !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek)
            }

            Row(modifier = Modifier.drawBehind {
                if (isSelectedWeek) {
                    val padding = 4.dp.toPx()
                    val width = size.width
                    val height = size.height
                    drawRoundRect(
                        color = AccentBlue,
                        size = Size(width, height),
                        topLeft = Offset(0f, padding),
                        cornerRadius = CornerRadius(18f)
                    )
                }
            }) {
                repeat(7) {
                    val item =
                        if (index < dates.size) dates[index] else CalendarUiState.Date.Empty
                    val hasLabel =
                        labels.any { it.dayOfMonth == item.dayOfMonth }

                    ContentItem(
                        date = item,
                        onClickListener = onDateClickListener,
                        modifier = Modifier.weight(1f),
                        hasLabel = hasLabel
                    )
                    index++
                }

            }
        }
    }
}


@Composable
fun ContentItem(
    modifier: Modifier = Modifier,
    date: CalendarUiState.Date,
    onClickListener: (LocalDate) -> Unit,
    hasLabel: Boolean = false
) {
    Column(
        modifier = modifier
            .noRippleClickable {
                onClickListener(date.localDate)
            },
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (date.dayOfMonth == -1) "" else date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (date.isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (date.localDate.dayOfWeek == DayOfWeek.SATURDAY || date.localDate.dayOfWeek == DayOfWeek.SUNDAY) HighlightBlue else Gray100
            ),
            modifier = Modifier
                .padding(top = 11.dp, bottom = 2.dp)
        )
        if (hasLabel)
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Yellow)
                    .size(5.dp)
            )
        else
            Box(
                modifier = Modifier
                    .size(5.dp)
            )
    }
}

@Composable
fun Header(
    yearMonth: YearMonth,
    onPreviousMonthButtonClicked: () -> Unit,
    onNextMonthButtonClicked: () -> Unit,
) {
    val monthText = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val yearText = yearMonth.year.toString()

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = "",
            tint = Gray100,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .noRippleClickable {
                    onPreviousMonthButtonClicked.invoke()
                }
        )

        Text(
            text = "$monthText, $yearText",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(color = Gray100),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "",
            tint = Gray100,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .noRippleClickable {
                    onNextMonthButtonClicked.invoke()
                }
        )

    }
}

@Preview
@Composable
fun PreviewCalendar() = BasePreviewContainer {
    Calendar(uiState = CalendarUiState(), labels = emptyList(), selectedDate = LocalDate.now())
}