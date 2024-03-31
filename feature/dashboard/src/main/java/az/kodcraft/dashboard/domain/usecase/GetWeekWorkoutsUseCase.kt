package az.kodcraft.dashboard.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.dashboard.domain.model.DashboardWeekWorkoutDm
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeekWorkoutsUseCase @Inject constructor() :
    BaseUseCase<Int, Response<List<DashboardWeekWorkoutDm>>> {
    override suspend fun execute(param:Int): Flow<Response<List<DashboardWeekWorkoutDm>>> = flow {
        emit(
            Response.Success(
                listOf(DashboardWeekWorkoutDm.MOCK, DashboardWeekWorkoutDm.MOCK2)
            )
        )
    }


}