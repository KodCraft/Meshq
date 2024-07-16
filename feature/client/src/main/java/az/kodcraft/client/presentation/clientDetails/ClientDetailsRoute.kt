package az.kodcraft.client.presentation.clientDetails

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
import az.kodcraft.core.presentation.theme.PrimaryRed
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.presentation.theme.header
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import java.time.DayOfWeek
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
    ClientDetailsScreen(
        clientDetails = uiState.clientDetails,
        selectedDate = uiState.selectedDay,
        isLoading = uiState.isLoading,
        isScheduleLoading = uiState.isScheduleLoading,
        onIntent = viewModel::acceptIntent
    )
}


@Composable
fun ClientDetailsScreen(
    clientDetails: ClientDm,
    selectedDate: LocalDate,
    isLoading: Boolean = false,
    isScheduleLoading: Boolean = false,
    onIntent: (ClientDetailsIntent) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header Section
        HeaderSection(clientDetails, isLoading)

        // Tabs
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabs = listOf("Calendar", "Progress", "Goals")

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
                workoutSchedule = clientDetails.workoutSchedule,
                selectedDate = selectedDate
            )

            1 -> ProgressSection()  // Implement this based on your progress data
            2 -> GoalsSection()     // Implement this based on your goals data
        }
        Box {
            HorizontalDivider()
            if (isScheduleLoading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = PrimaryTurq,
                    trackColor = AccentBlue
                )
        }
        Spacer(modifier = Modifier.height(10.dp))
        WeeklyWorkoutsSection(
            clientDetails.filterWorkoutScheduleForWeek(selectedDate),
            selectedDate
        )
    }
}

@Composable
fun HeaderSection(clientDetails: ClientDm, isLoading: Boolean) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(AccentBlue),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (isLoading)
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = PrimaryTurq,
                    trackColor = AccentBlue
                )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            if (isLoading)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(100.dp)
                ) {
                    ShimmerEffect()
                }
            else
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
                } ?: Image(
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
                text = if (isLoading) "" else {
                    "@" + clientDetails.username
                },
                color = Color.Gray,
                style = MaterialTheme.typography.body,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CalendarSection(
    onMonthChanged: (YearMonth) -> Unit,
    onDateClick: (LocalDate) -> Unit,
    workoutSchedule: List<ClientDm.WorkoutSessionDm>,
    selectedDate: LocalDate
) {
    Column(modifier = Modifier.padding(horizontal = 26.dp).height(300.dp)) {
        MonthlyCalendar(
            onDateClick = onDateClick,
            onMonthChanged = onMonthChanged,
            labels = workoutSchedule.map { it.date },
            selectedDate = selectedDate
        )
    }
}


@Composable
fun WeeklyWorkoutsSection(
    workoutSchedule: List<ClientDm.WorkoutSessionDm>,
    selectedDate: LocalDate
) {
    // Map workouts to corresponding DayOfWeek
    val workoutsByDay = DayOfWeek.entries.associateWith { day ->
        workoutSchedule.firstOrNull { it.date.dayOfWeek == day }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(workoutsByDay.entries.toList()) { (day, workout) ->
            val today = selectedDate.with(day)
            val formattedDate = today.formatDateToWeeklyStringDayAndMonth()
            DayWithWorkoutItem(formattedDate, workout)
        }
    }
}

@Composable
fun DayWithWorkoutItem(formattedDate: String, workout: ClientDm.WorkoutSessionDm?) {
    Column(Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom,
            ) {
                DateDisplay(formattedDate)
                Text(
                    text = workout?.workoutName ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
            Icon(
                painter = if (workout == null) painterResource(id = az.kodcraft.core.R.drawable.ic_add_circle) else painterResource(
                    id = az.kodcraft.core.R.drawable.ic_remove_circle
                ),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (workout == null) PrimaryTurq.copy(0.7f) else PrimaryRed
            )

        }
        HorizontalDivider(color = Color.White.copy(0.5f))
    }
}


@Composable
fun DateDisplay(dateString: String) {
    val parts = dateString.split(" ")
    val dayOfWeek = parts[0]
    val date = parts.subList(1, parts.size).joinToString(" ")

    Text(
        modifier = Modifier.width(80.dp),
        style = MaterialTheme.typography.body,
        maxLines = 1,
        color = Color.White.copy(0.5f),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ),
            ) {
                append(dayOfWeek)
            }
            append("  ")
            append(date)
        }
    )
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
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 15), "Legs (Glute focused)", true),
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 17), "Arms and Back", false),
        ClientDm.WorkoutSessionDm(
            LocalDate.of(2024, 7, 19),
            "Upper body Chest Focused Workout template 2",
            false
        ),
        ClientDm.WorkoutSessionDm(LocalDate.of(2024, 7, 21), "Legs and Core", true)
    )
    val sampleClient = ClientDm.MOCK.copy(workoutSchedule = sampleWorkouts)
    ClientDetailsScreen(clientDetails = sampleClient, selectedDate = LocalDate.now())
}