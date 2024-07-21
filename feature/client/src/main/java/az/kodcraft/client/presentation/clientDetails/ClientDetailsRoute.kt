package az.kodcraft.client.presentation.clientDetails

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.presentation.clientDetails.composables.DateDisplay
import az.kodcraft.client.presentation.clientDetails.composables.HeaderSection
import az.kodcraft.client.presentation.clientDetails.composables.TabsSection
import az.kodcraft.client.presentation.clientDetails.composables.WeeklyWorkoutsSection
import az.kodcraft.client.presentation.clientDetails.composables.WorkoutSearchBottomSheetContent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsEvent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsIntent
import az.kodcraft.client.presentation.clientDetails.contract.ClientDetailsUiState
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.button.ButtonPrimaryLightWithLoader
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.Gray100
import az.kodcraft.core.presentation.theme.PrimaryBlue
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import az.kodcraft.core.utils.noRippleClickable
import java.time.LocalDate
import java.util.Calendar


@Composable
fun ClientDetailsRoute(
    viewModel: ClientDetailsViewModel = hiltViewModel(),
    userId: String,
    navigateBack: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.acceptIntent(ClientDetailsIntent.GetClientDetails(userId))
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect{
            when(it){
                ClientDetailsEvent.NavigateToDashboard -> navigateBack()
            }
        }

    }
    val uiState by viewModel.uiState.collectAsState()
    ClientDetailsScreen(
        uiState = uiState,
        onIntent = viewModel::acceptIntent,
        navigateBack = navigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientDetailsScreen(
    onIntent: (ClientDetailsIntent) -> Unit = {},
    navigateBack: () -> Unit = {},
    uiState: ClientDetailsUiState
) {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        az.kodcraft.core.R.style.CustomDatePickerDialog,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            onIntent.invoke(
                ClientDetailsIntent.SetDateForWorkoutToAssign(
                    LocalDate.of(
                        selectedYear,
                        selectedMonth,
                        selectedDayOfMonth
                    )
                )
            )
        }, year, month, day
    )

    if (uiState.workoutToAssign.workout != WorkoutListItemDm.EMPTY) {
        Dialog(onDismissRequest = {
            onIntent.invoke(
                ClientDetailsIntent.SelectWorkoutToAssign(
                    WorkoutListItemDm.EMPTY
                )
            )
        }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(AccentBlue)
                    .padding(24.dp)
            ) {
                Icon(
                    painterResource(id = az.kodcraft.core.R.drawable.ic_close),
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.End),
                    contentDescription = ""
                )
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = "Are you sure  you want to assign this workout?",
                    style = MaterialTheme.typography.body
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier,
                    text = uiState.workoutToAssign.workout.name,
                    style = MaterialTheme.typography.body
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6f))
                            .background(Gray100.copy(0.5f)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DateDisplay(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp),
                            color = Color.White,
                            dateString = uiState.workoutToAssign.date.formatDateToWeeklyStringDayAndMonth()
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            painter = painterResource(id = az.kodcraft.core.R.drawable.ic_calendar),
                            contentDescription = "",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(16.dp)
                                .noRippleClickable { datePickerDialog.show() }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    ButtonPrimaryLightWithLoader(
                        text = "Assign",
                        iconResId = az.kodcraft.core.R.drawable.ic_add_data,
                        isLoading = uiState.isAssignmentLoading,
                        onClick = { onIntent.invoke(ClientDetailsIntent.AssignWorkout) }
                    )
                }
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) { // Header Section
        HeaderSection(uiState.clientDetails, uiState.isLoading, navigateBack)

        TabsSection(uiState.clientDetails, uiState.selectedDay, onIntent)

        WeeklyWorkoutsSection(
            uiState.clientDetails.filterWorkoutScheduleForWeek(uiState.selectedDay),
            uiState.selectedDay,
            uiState.isScheduleLoading,
            onAddWorkoutClicked = {
                onIntent.invoke(ClientDetailsIntent.SetDateForWorkoutToAssign(it))
            }
        )

    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (uiState.showSheet) {
        ModalBottomSheet(
            modifier = Modifier.padding(top = 50.dp),
            onDismissRequest = { onIntent.invoke(ClientDetailsIntent.HideSheet) },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White) },
            containerColor = PrimaryBlue,
            scrimColor = Gray100.copy(0.2f)
        ) {
            WorkoutSearchBottomSheetContent(
                workouts = uiState.filteredWorkouts,
                filter = uiState.workoutsFilter,
                onResetFilter = {
                    onIntent.invoke(
                        ClientDetailsIntent.ResetFilter
                    )
                },
                onSearchValueChanged = {
                    onIntent.invoke(
                        ClientDetailsIntent.FilterWorkoutsSearchText(
                            it
                        )
                    )
                },
                onFilterTagClicked = {
                    onIntent.invoke(
                        ClientDetailsIntent.FilterWorkoutsTags(
                            it
                        )
                    )
                },
                isLoading = sheetState.isVisible && uiState.areWorkoutsLoading,
                onWorkoutCLicked = { onIntent.invoke(ClientDetailsIntent.SelectWorkoutToAssign(it)) }
            )
        }
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
    ClientDetailsScreen(
        uiState = ClientDetailsUiState().copy(clientDetails = sampleClient)
    )
}