package az.kodcraft.dashboard.domain.repository

import androidx.paging.PagingData
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TrainerDashboardRepository {
    fun getWorkouts(date: LocalDate): Flow<PagingData<TrainerDashboardDayDm>>
}