package az.kodcraft.dashboard.data.pagingSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import az.kodcraft.core.utils.formatDateToWeeklyStringDayAndMonth
import az.kodcraft.core.utils.localDateToTimestamp
import az.kodcraft.core.utils.toLocalDate
import az.kodcraft.dashboard.data.dto.trainer.TrainerDashboardWorkoutDto
import az.kodcraft.dashboard.domain.model.TrainerDashboardDayDm
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class WorkoutPagingSource(
    private val workoutsQuery: Query,
    private val workoutsQueryDesc: Query,
    private val usersCollection: CollectionReference,
    private val startDate: LocalDate
) : PagingSource<List<DocumentSnapshot>, TrainerDashboardDayDm>() {

    override suspend fun load(params: LoadParams<List<DocumentSnapshot>>): LoadResult<List<DocumentSnapshot>, TrainerDashboardDayDm> {

        return try {
            val currentPage = params.key ?: workoutsQuery
                .whereGreaterThanOrEqualTo("date", localDateToTimestamp(startDate))
                .get().await().documents

            val firstVisible = currentPage[0]
            val lastVisible = currentPage[currentPage.size - 1]
            val nextPage = workoutsQuery.startAfter(lastVisible).get().await().documents
            val prevPage = workoutsQueryDesc.startAfter(firstVisible).get().await().documents.reversed()
            Log.d(
                "PAGING_FETCH",
                "Current Page: " + currentPage.map {
                    it.getTimestamp("date")?.toLocalDate()?.formatDateToWeeklyStringDayAndMonth()
                })
            Log.d(
                "PAGING_FETCH",
                "First Visible: " + firstVisible.getTimestamp("date")?.toLocalDate()
                    ?.formatDateToWeeklyStringDayAndMonth()
            )
            Log.d(
                "PAGING_FETCH",
                "Last Visible : " + lastVisible.getTimestamp("date")?.toLocalDate()
                    ?.formatDateToWeeklyStringDayAndMonth()
            )
            Log.e("PAGING_FETCH", "--------------------")

            LoadResult.Page(
                data = currentPage.mapNotNull { doc ->
                    doc.toObject(TrainerDashboardWorkoutDto::class.java)?.apply { id = doc.id }
                }.groupBy { it.date.toLocalDate().atStartOfDay() }.map { entry ->
                    TrainerDashboardDayDm(
                        date = entry.key.toLocalDate(),
                        sessions = entry.value.map {
                            TrainerDashboardDayDm.DayTraineesDm(
                                id = it.id,
                                traineeName = usersCollection.document(it.traineeId).get().await()
                                    .get("name").toString().ifEmpty { "N/A" },
                                workoutTitle = it.title
                            )
                        })
                },
                nextKey = nextPage,
                prevKey = prevPage
            )
        } catch (e: Exception) {
            // Log.e("PAGING_FETCH", e.localizedMessage, e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<List<DocumentSnapshot>, TrainerDashboardDayDm>): List<DocumentSnapshot>? {
        return null
    }
}