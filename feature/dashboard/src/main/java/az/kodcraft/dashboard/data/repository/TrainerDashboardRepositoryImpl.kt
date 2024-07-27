package az.kodcraft.dashboard.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import az.kodcraft.dashboard.data.di.DASHBOARD_PAGE_SIZE
import az.kodcraft.dashboard.data.pagingSource.WorkoutPagingSource
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import az.kodcraft.dashboard.domain.repository.TrainerDashboardRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class TrainerDashboardRepositoryImpl(
    private val queryWorkoutsByDate: Query,
    private val queryWorkoutsByDateDesc: Query,
    private val usersCollection: CollectionReference
) :
    TrainerDashboardRepository {

    override fun getWorkouts(date: LocalDate): Flow<PagingData<TrainerDashboardDayDm>> {
        return Pager(
            config = PagingConfig(pageSize = DASHBOARD_PAGE_SIZE, prefetchDistance =  2),
            pagingSourceFactory = {
                WorkoutPagingSource(queryWorkoutsByDate,queryWorkoutsByDateDesc , usersCollection, date)
            }
        ).flow
    }
}