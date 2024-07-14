package az.kodcraft.client.presentation.clientDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsIntent
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.calendar.MonthlyCalendar
import az.kodcraft.core.presentation.composable.image.ShimmerEffect
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.header
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun ClientDetailsRoute(
    viewModel: ClientDetailsViewModel = hiltViewModel(),
    userId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.acceptIntent(ClientDetailsIntent.GetClientDetails(userId))
    }
    val uiState by viewModel.uiState.collectAsState()
    ClientDetailsScreen(uiState.clientDetails, uiState.selectedDay, viewModel::acceptIntent)
}


@Composable
fun ClientDetailsScreen(
    clientDetails: ClientDm,
    selectedDate: LocalDate,
    onIntent: (ClientDetailsIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header Section
        HeaderSection(clientDetails)

        // Tabs
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("Calendar", "Progress", "Goals")

        HorizontalDivider()
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = Color.White,
            containerColor = PrimaryBlue,
            indicator = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            title,
                            style = if (selectedTabIndex == index) MaterialTheme.typography.body.copy(
                                fontWeight = FontWeight.Bold
                            ) else MaterialTheme.typography.body
                        )
                    }
                )
            }
        }


        // Content
        Spacer(modifier = Modifier.height(10.dp))
        when (selectedTabIndex) {
            0 -> CalendarSection(
                onDateClick = { onIntent.invoke(ClientDetailsIntent.SelectDate(it)) },
                onMonthChanged = {
                    onIntent.invoke(
                        ClientDetailsIntent.GetMonthWorkouts(
                            it
                        )
                    )
                },
                workoutSchedule = clientDetails.workoutSchedule
            )

            1 -> ProgressSection()  // Implement this based on your progress data
            2 -> GoalsSection()     // Implement this based on your goals data
        }
        HorizontalDivider()
        WeeklyWorkoutsSection(clientDetails.filterWorkoutScheduleForWeek(selectedDate))
    }
}

@Composable
fun HeaderSection(clientDetails: ClientDm) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(AccentBlue)
        ) {

        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
        ) {
            Spacer(modifier = Modifier.height(45.dp))
            clientDetails.profilePicUrl.ifEmpty { null }?.let { imageUrl ->
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build(),
                    contentDescription = "Profile image",
                    contentScale = ContentScale.FillWidth,
                    loading = {
                        ShimmerEffect()
                    },
                    error = {
                        Image(
                            painter = painterResource(id = az.kodcraft.core.R.drawable.profile_image_placeholder),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                )
            } ?: Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.profile_image_placeholder),
                contentDescription = "ProfileImage",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = clientDetails.name,
                color = Color.White,
                style = MaterialTheme.typography.header
            )
            Text(
                text = "@" + clientDetails.username,
                color = Color.Gray,
                style = MaterialTheme.typography.body,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun CalendarSection(
    onMonthChanged: (YearMonth) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    workoutSchedule: List<ClientDm.WorkoutSessionDm>
) {
    Column(modifier = Modifier.height(300.dp)) {
        MonthlyCalendar(
            onDateClick = onDateClick,
            onMonthChanged = onMonthChanged,
            labels = workoutSchedule.map { it.date }
        )
    }
}

@Composable
fun WeeklyWorkoutsSection(workoutSchedule: List<ClientDm.WorkoutSessionDm>) {
    // A simple list of workouts for now. You can expand this to a full calendar view.
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(workoutSchedule) { workout ->
            WorkoutItem(workout)
        }
    }
}

@Composable
fun WorkoutItem(workout: ClientDm.WorkoutSessionDm) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = workout.date.formatDateToWeeklyStringDayAndMonth(),
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = workout.workoutName,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.weight(2f)
        )
        Icon(
            imageVector = if (workout.isCompleted) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (workout.isCompleted) Color.Green else Color.Red
        )
    }
}

// Dummy composable functions for the other tabs
@Composable
fun ProgressSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Progress Section", color = Color.White, fontSize = 20.sp)
    }
}

@Composable
fun GoalsSection() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Goals Section", color = Color.White, fontSize = 20.sp)
    }
}

@Preview()
@Composable
fun ClientDetailsScreenPreview() = BasePreviewContainer {
    val sampleWorkouts = listOf(
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 14), "Legs", true),
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 16), "Arms", false),
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 17), "Back", true)
    )
    val sampleClient = ClientDm.MOCK.copy(workoutSchedule = sampleWorkouts)
    ClientDetailsScreen(clientDetails = sampleClient, selectedDate = LocalDate.now())
}