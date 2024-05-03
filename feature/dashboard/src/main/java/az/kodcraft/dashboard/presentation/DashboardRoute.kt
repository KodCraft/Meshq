package az.kodcraft.dashboard.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryLight
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.bodyLight
import az.kodcraft.core.presentation.theme.footNote
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.presentation.theme.smallTitle
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.R
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.presentation.composables.WeekRow
import az.kodcraft.dashboard.presentation.contract.DashboardIntent
import az.kodcraft.dashboard.presentation.contract.DashboardUiState
import az.kodcraft.dashboard.presentation.utils.getWeekData
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel(),
    padding: PaddingValues,
    navigateToWorkoutDetails: (id: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(
        uiState,
        padding = padding,
        onIntent = { viewModel.acceptIntent(it) },
        onWorkoutClick = { navigateToWorkoutDetails(it) })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onIntent: (DashboardIntent) -> Unit = {},
    onWorkoutClick: (id: String) -> Unit = {},
    padding: PaddingValues
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(showMenuIcon = true)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = stringResource(R.string.dashboard_screen_title),
                color = Color.White,
                style = MaterialTheme.typography.largeTitle
            )

            val listState = rememberLazyListState(
                initialFirstVisibleItemIndex = uiState.startIndex,
            )
            // Observe the first visible item index
            val visibleWeekIndex by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex
                }
            }

            MonthDropdown(
                modifier = Modifier.align(Alignment.End),
                selectedMonth = uiState.selectedDay.month.getDisplayName(
                    TextStyle.FULL,
                    Locale.getDefault()
                ),
                onMonthSelected = { onIntent.invoke(DashboardIntent.SetDay(it, visibleWeekIndex)) },
                year = uiState.selectedDay.year
            )

            // Update selected day when week changes
            LaunchedEffect(visibleWeekIndex) {
                val weekData = uiState.selectedWeek(visibleWeekIndex).getWeekData()
                val dayIndex = uiState.selectedDay.dayOfWeek.value - 1
                if (dayIndex != -1) {
                    onIntent.invoke(
                        DashboardIntent.SetDay(
                            weekData.weekDays[dayIndex].day,
                            visibleWeekIndex
                        )
                    )
                }
            }


            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(
                    lazyListState = listState
                )
            ) {

                items(PageCount) { index ->
                    uiState.selectedWeek(index).getWeekData().apply {
                        WeekRow(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 6.dp),
                            data = weekDays,
                            selectedDay = uiState.selectedDay,
                            onDayClicked = { onIntent.invoke(DashboardIntent.SetDay(it, index)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))
            if (uiState.isLoading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )

            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(
                visible = uiState.isLoading.not() && uiState.weekWorkouts.any(),
                enter = expandHorizontally(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    ), expandFrom = Alignment.Start
                ),
                exit = fadeOut()
            ) {
                Spacer(modifier = Modifier.height(2.dp))
                Workouts(
                    workouts = uiState.weekWorkouts,
                    onWorkoutClick = onWorkoutClick,
                )
            }
            if (uiState.weekWorkouts.isEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.dashboard_screen_no_data),
                    style = MaterialTheme.typography.body,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

        }
    }
}

const val PageCount = 20_000
const val StartIndex = PageCount / 2


@Composable
fun MonthDropdown(
    selectedMonth: String, // This could be adjusted to use LocalDate or another appropriate type
    onMonthSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    year: Int  // Default to current year, adjustable as needed
) {
    var expanded by remember { mutableStateOf(false) }
    var displayMonth by remember(selectedMonth) { mutableStateOf(selectedMonth) }

    // Generate month names using LocalDate and the default locale
    val months = (1..12).map {
        LocalDate.of(2022, it, 1).month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = displayMonth,
                style = MaterialTheme.typography.bodyLight,
                modifier = Modifier
                    .noRippleClickable { expanded = true }
                    .padding(8.dp)
            )
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_dropdown),
                contentDescription = "month dropdown",
                modifier = Modifier.size(16.dp)
            )
        }

        // Dropdown menu that shows when expanded is true
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .height(160.dp)  // Adjust height to fit the UI design
                .padding(top = 4.dp)
        ) {
            months.forEachIndexed { index, month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        displayMonth = month
                        // Calculate the first day of the selected month
                        val firstDayOfMonth = LocalDate.of(year, index + 1, 1)
                        onMonthSelected(firstDayOfMonth)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun Workouts(
    workouts: List<DashboardWeekWorkoutDm>,
    onWorkoutClick: (id: String) -> Unit
) {
    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        items(workouts) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(185.dp)
                    .noRippleClickable { onWorkoutClick(it.id) },
                colors = if (it.isSelected) CardDefaults.cardColors()
                    .copy(containerColor = PrimaryTurq) else CardDefaults.cardColors().copy(containerColor = PrimaryLight)
            ) {
                Box(Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = it.title,
                            style = if (it.isSelected) MaterialTheme.typography.smallTitle.copy(
                                color = Color.White
                            )
                            else MaterialTheme.typography.smallTitle
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it.content,
                            style = if (it.isSelected) MaterialTheme.typography.body.copy(color = Color.White)
                            else MaterialTheme.typography.body
                        )
                    }
                    WorkoutDate(
                        it.date, modifier = Modifier
                            .padding(top = 18.dp, end = 25.dp)
                            .align(Alignment.TopEnd)
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        if (it.isSelected) PrimaryTurq.copy(0.95f) else Color.White.copy(
                                            alpha = 0.95f
                                        )
                                    ),
                                    startY = 300f, // You might need to adjust this value
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun WorkoutDate(date: String, modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = az.kodcraft.core.R.drawable.ic_calendar),
            contentDescription = "calendar",
            tint = Color.Black,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = date, style = MaterialTheme.typography.footNote.copy(color = Color.Black))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() = BasePreviewContainer {
    DashboardScreen(
        DashboardUiState(),
        padding = PaddingValues(0.dp)
    )
}




