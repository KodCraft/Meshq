package az.kodcraft.dashboard.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.dashboard.domain.model.DashboardWeekDm
import az.kodcraft.dashboard.domain.model.DayOfWeekDm
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

class GetWeekDataUseCase @Inject constructor() : BaseUseCase<Int, Response<DashboardWeekDm>> {

    override suspend fun execute(param: Int): Flow<Response<DashboardWeekDm>> = flow {
        val today = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val firstDayOfYear = today.with(TemporalAdjusters.firstDayOfYear())
        val firstWeekStart = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

        // Calculate the first day of the given week of the year
        val daysToAdd = (param - firstDayOfYear.get(weekFields.weekOfWeekBasedYear())) * 7L
        val weekStart = firstWeekStart.plusDays(daysToAdd)

        val weekDays = (0..6).map { offset ->
            val date = weekStart.plusDays(offset.toLong())

            DayOfWeekDm(
                day = date,
                workoutId = "" // Populate as needed
            )
        }

        emit(
            Response.Success(
                DashboardWeekDm(
                    weekDays = weekDays
                )
            )
        )
    }
}
