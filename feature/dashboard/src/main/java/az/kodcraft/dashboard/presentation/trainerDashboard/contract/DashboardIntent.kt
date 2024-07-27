package az.kodcraft.dashboard.presentation.trainerDashboard.contract

sealed class TrainerDashboardIntent {
    data object Init : TrainerDashboardIntent()
}
