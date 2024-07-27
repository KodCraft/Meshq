package az.kodcraft.dashboard.presentation.trainerDashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.appBar.TopAppBar
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.PrimaryLight
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.bodyLarge
import az.kodcraft.core.presentation.theme.largeHeadLine
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardIntent
import az.kodcraft.dashboard.presentation.trainerDashboard.contract.TrainerDashboardUiState
import kotlinx.coroutines.flow.flow
import java.time.LocalDate


@Composable
fun TrainerDashboardRoute(
    viewModel: TrainerDashboardViewModel = hiltViewModel(),
    onNavigateToCreateWorkout: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    switchMode: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    TrainerDashboardScreen(
        uiState,
        onIntent = { viewModel.acceptIntent(it) },
        onNavigateToCreateWorkout = onNavigateToCreateWorkout,
        onNavigateToNotifications = onNavigateToNotifications,
        switchMode = switchMode,
        onMenuClick = onMenuClick
    )
}

@Composable
fun TrainerDashboardScreen(
    uiState: TrainerDashboardUiState,
    onIntent: (TrainerDashboardIntent) -> Unit = {},
    onNavigateToCreateWorkout: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    switchMode: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(showMenuIcon = true, onMenuClick = onMenuClick) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                UserManager.getUserFullName(),
                style = MaterialTheme.typography.body,
                modifier = Modifier.padding(end = 12.dp)
            )
            NotificationIcon(onNavigateToNotifications)
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_profile),
                tint = Color.White,
                contentDescription = "go to profile",
                modifier = Modifier.noRippleClickable { switchMode() }
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            val lazyWorkoutItems = remember(uiState.workouts) {
                flow {
                    emit(uiState.workouts)
                }
            }.collectAsLazyPagingItems()
            val listState = rememberLazyListState()
            val today = LocalDate.now()
            var isLoading by remember { mutableStateOf(false) }


            // Use derivedStateOf to track when the list of items is available and trigger scrolling
            val targetIndex by remember {
                derivedStateOf {
                    if (lazyWorkoutItems.itemSnapshotList.size == 0) -1
                    else
                        lazyWorkoutItems.itemSnapshotList.items.let { items ->
                            lazyWorkoutItems.itemSnapshotList.items.indexOfFirst {
                                it.date.isEqual(today) || it.date.isAfter(today)
                            }.takeIf { it != -1 } ?: 0
                        }
                }
            }


//            LaunchedEffect(lazyWorkoutItems.itemSnapshotList) {
//                if (targetIndex == 0)
//                    isLoading = false
//                else if (targetIndex > 0) {
//                    listState.animateScrollToItem(targetIndex)
//                    isLoading = false
//                }
//            }

            val viewportSize = 10 // Adjust this value based on your screen size and item height

            if (isLoading)
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(PrimaryBlue),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryTurq
                    )
                }
            else
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState, modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            lazyWorkoutItems.itemCount,
                            key = lazyWorkoutItems.itemKey { it.sessions.joinToString { it.id } }) { index ->
                            lazyWorkoutItems[index]?.let { workout ->
                                DashboardItem(workout)
                            }
                        }

                        if (targetIndex in 0 until lazyWorkoutItems.itemCount) {
                            val remainingItems = lazyWorkoutItems.itemCount - targetIndex
                            val extraItemsNeeded = viewportSize - remainingItems
                            if (extraItemsNeeded > 0) {
                                item {
                                    Spacer(modifier = Modifier.height((extraItemsNeeded * 64).dp)) // Adjust the item height as needed
                                }
                            }
                        }

                    }

                    lazyWorkoutItems.apply {
                        when {

                            loadState.refresh is LoadState.Loading -> LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopStart)
                                    .height(1.dp), color = PrimaryTurq
                            )

                            loadState.append is LoadState.Loading -> LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopStart)
                                    .height(1.dp), color = PrimaryTurq
                            )

                            loadState.refresh is LoadState.Error -> {
                                //  Toast.makeText(context, "System error", Toast.LENGTH_SHORT).show()
                            }

                            loadState.append is LoadState.Error -> {
                                //  Toast.makeText(context, "System error", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            FloatingActionButton(
                onClick = onNavigateToCreateWorkout,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = PrimaryLight
            ) {
                Icon(
                    painter = painterResource(id = az.kodcraft.core.R.drawable.ic_add_data),
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun DashboardItem(data: TrainerDashboardDayDm) {
    Column(Modifier.padding(vertical = 20.dp)) {


        Text(
            text = data.date.formatDateToWeeklyStringDayAndMonth(),
            style = MaterialTheme.typography.largeHeadLine
        ) // Replace with your workout item composable

        data.sessions.forEach { session ->
            Row(
                Modifier
                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "– " + session.traineeName,
                    style = bodyLarge.copy(fontSize = 24.sp),
                    modifier = Modifier
                ) // Replace with your workout item composable

                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryLight.copy(0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = session.workoutTitle,
                        style = MaterialTheme.typography.body,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) // Replace with your workout item composable
                }
            }
        }
    }
}

@Composable
fun NotificationIcon(onClick: () -> Unit) {

    Icon(
        painter = painterResource(id = az.kodcraft.core.R.drawable.ic_notification_bell),
        tint = Color.White,
        contentDescription = "go to notifications",
        modifier = Modifier
            .noRippleClickable { onClick() }
            .padding(end = 6.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewTrainerDashboard() = BasePreviewContainer {
    TrainerDashboardScreen(
        TrainerDashboardUiState(
            workouts = PagingData.from(
                listOf(
                    TrainerDashboardDayDm(
                        date = LocalDate.now(),
                        sessions = listOf(
                            TrainerDashboardDayDm.DayTraineesDm(
                                id = "id",
                                workoutTitle = "Push and GLutes",
                                traineeName = "Aslan"
                            )
                        )
                    ),
                    TrainerDashboardDayDm(
                        date = LocalDate.now().plusDays(2),
                        sessions = listOf(
                            TrainerDashboardDayDm.DayTraineesDm(
                                id = "id2",
                                workoutTitle = "Upper Body",
                                traineeName = "Aslan"
                            ),
                            TrainerDashboardDayDm.DayTraineesDm(
                                id = "id3",
                                workoutTitle = "Full Body",
                                traineeName = "Natavan"
                            )
                        )
                    )
                )
            )
        )
    )
}




