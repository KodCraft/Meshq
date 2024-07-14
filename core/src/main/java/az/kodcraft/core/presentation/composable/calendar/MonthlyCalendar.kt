package az.kodcraft.core.presentation.composable.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import az.kodcraft.core.utils.noRippleClickable
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun MonthlyCalendar(
    viewModel: CalendarViewModel = hiltViewModel(),
    onDateClick: (LocalDate) -> Unit = {},
    onMonthChanged: (YearMonth) -> Unit = {},
    labels: List<LocalDate> = emptyList()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                is CalendarEvent.MonthChanged -> onMonthChanged(it.yearMonth)
            }
        }
    }
    Calendar(uiState = uiState,
        onDateClick = onDateClick,
        labels = labels,
        onPreviousMonthButtonClicked = { viewModel.acceptIntent(CalendarIntent.PreviousMonthClicked) },
        onNextMonthButtonClicked = { viewModel.acceptIntent(CalendarIntent.NextMonthClicked) })
}

@Composable
fun Calendar(
    uiState: CalendarUiState,
    onPreviousMonthButtonClicked: () -> Unit = {},
    onNextMonthButtonClicked: () -> Unit = {},
    onDateClick: (LocalDate) -> Unit = {},
    labels: List<LocalDate> = emptyList()
) {
    Column {
        Header(
            yearMonth = uiState.yearMonth,
            onPreviousMonthButtonClicked = onPreviousMonthButtonClicked,
            onNextMonthButtonClicked = onNextMonthButtonClicked
        )
        Content(
            uiState.dates,
            onDateClickListener = onDateClick,
            labels = labels.filter { YearMonth.from(it) == uiState.yearMonth })
    }
}

@Composable
fun Content(
    dates: List<CalendarUiState.Date>,
    onDateClickListener: (LocalDate) -> Unit,
    labels: List<LocalDate> = emptyList()
) {
    Column {
        var index = 0
        repeat(6) {
            if (index >= dates.size) return@repeat
            Row {
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date.Empty
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
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (date.isSelected) FontWeight.Bold else FontWeight.Normal),
            modifier = Modifier
                .padding(10.dp)
        )
        if (hasLabel)
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .clip(CircleShape)
                    .size(3.dp)
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

    Row {
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke()
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = ""
            )
        }
        Text(
            text = "$monthText, $yearText",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {
            onNextMonthButtonClicked.invoke()
        }) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
fun PreviewCalendar() = BasePreviewContainer {
    Calendar(uiState = CalendarUiState(), labels = emptyList())
}