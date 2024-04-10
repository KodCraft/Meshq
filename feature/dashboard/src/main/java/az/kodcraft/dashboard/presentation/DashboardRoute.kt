package az.kodcraft.dashboard.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.footNote
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.presentation.theme.smallTitle
import az.kodcraft.dashboard.R
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.presentation.composables.WeekRow
import az.kodcraft.dashboard.presentation.contract.DashboardIntent
import az.kodcraft.dashboard.presentation.contract.DashboardUiState
import az.kodcraft.dashboard.presentation.utils.getWeekData


@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateToWorkoutDetails: (id: String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(
        uiState,
        onIntent = { viewModel.acceptIntent(it) },
        onWorkoutClick = { navigateToWorkoutDetails(it) })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onIntent: (DashboardIntent) -> Unit = {},
    onWorkoutClick: (id: String) -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(showMenuIcon = true)
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 6.dp),
                text = stringResource(R.string.dashboard_screen_title),
                color = Color.White,
                style = MaterialTheme.typography.largeTitle
            )
            Spacer(modifier = Modifier.height(30.dp))


            val listState = rememberLazyListState(
                initialFirstVisibleItemIndex = uiState.startIndex,
            )

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

            Spacer(modifier = Modifier.height(30.dp))
            if (uiState.isLoading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                )
            else {
                Spacer(modifier = Modifier.height(1.dp))

                Workouts(workouts = uiState.weekWorkouts, onWorkoutClick)
                if (uiState.weekWorkouts.isEmpty())
                    Text(
                        text = stringResource(R.string.dashboard_screen_no_data),
                        style = MaterialTheme.typography.body,
                        modifier = Modifier.padding(12.dp)
                    )
            }
        }
    }
}

const val PageCount = 20_000
const val StartIndex = PageCount / 2

@Composable
fun Workouts(workouts: List<DashboardWeekWorkoutDm>, onWorkoutClick: (id: String) -> Unit) {
    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(workouts) {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(185.dp)
                    .clickable { onWorkoutClick(it.id) },
                colors = if (it.isSelected) CardDefaults.cardColors()
                    .copy(containerColor = PrimaryTurq) else CardDefaults.cardColors()
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
                    WorkoutDate(it.date, modifier = Modifier.padding(top = 18.dp, end = 25.dp).align(Alignment.TopEnd))

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                       if(it.isSelected) PrimaryTurq.copy(0.95f) else  Color.White.copy(alpha = 0.95f)
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
fun WorkoutDate(date: String, modifier:Modifier) {
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
fun PreviewDashboard() {
    DashboardScreen(
        DashboardUiState()
    )
}



