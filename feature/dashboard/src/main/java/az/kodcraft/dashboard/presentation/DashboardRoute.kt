package az.kodcraft.dashboard.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.core.presentation.composable.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.largeTitle
import az.kodcraft.core.presentation.theme.smallTitle
import az.kodcraft.dashboard.R
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import az.kodcraft.dashboard.presentation.composables.WeekRow
import az.kodcraft.dashboard.presentation.contract.DashboardIntent
import az.kodcraft.dashboard.presentation.contract.DashboardUiState
import az.kodcraft.dashboard.presentation.utils.getWeekData
import java.time.temporal.WeekFields
import java.util.Locale


@Composable
fun DashboardRoute(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    DashboardScreen(uiState, onIntent = { viewModel.acceptIntent(it) }, {})
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
            .padding(horizontal = 24.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(showMenuIcon = true)
        Text(
            modifier = Modifier.padding(horizontal = 6.dp),
            text = stringResource(R.string.dashboard_screen_title),
            color = Color.White,
            style = MaterialTheme.typography.largeTitle
        )
        Spacer(modifier = Modifier.height(30.dp))
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = StartIndex,
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
                val selectedWeek = remember {
                    (uiState.selectedDay.get(
                        WeekFields.of(
                            Locale.getDefault()
                        ).weekOfWeekBasedYear()
                    ) + (index - StartIndex))
                }

                selectedWeek.getWeekData().apply {
                    WeekRow(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(horizontal = 6.dp),
                        data = weekDays,
                        selectedDay = uiState.selectedDay,
                        onDayClicked = { onIntent.invoke(DashboardIntent.SetDay(it)) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Workouts(workouts = uiState.weekWorkouts, onWorkoutClick)
    }
}

const val PageCount = 20_000
const val StartIndex = PageCount / 2

@Composable
fun Workouts(workouts: List<DashboardWeekWorkoutDm>, onWorkoutClick: (id: String) -> Unit) {
    LazyColumn(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        items(workouts) {
            Card(
                modifier = Modifier
                    .height(185.dp)
                    .clickable { onWorkoutClick(it.id) },
                colors = if (it.isSelected) CardDefaults.cardColors()
                    .copy(containerColor = PrimaryTurq) else CardDefaults.cardColors()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = it.title,
                        style = if (it.isSelected) MaterialTheme.typography.smallTitle.copy(color = Color.White)
                        else MaterialTheme.typography.smallTitle
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it.content,
                        style = if (it.isSelected) MaterialTheme.typography.body.copy(color = Color.White)
                        else MaterialTheme.typography.body
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDashboard() {
    DashboardScreen(
        DashboardUiState()
    )
}



