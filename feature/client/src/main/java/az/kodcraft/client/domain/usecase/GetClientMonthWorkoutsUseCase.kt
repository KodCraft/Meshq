package az.kodcraft.client.domain.usecase

import az.kodcraft.client.domain.model.ClientDetailsRequestDto
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.core.domain.bases.model.Response
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientMonthWorkoutsUseCase @Inject constructor(private val repository: ClientRepository) :
    BaseUseCase<ClientDetailsRequestDto, Response<List<ClientDm.WorkoutSessionDm>>> {
    override suspend fun execute(param:ClientDetailsRequestDto): Flow<Response<List<ClientDm.WorkoutSessionDm>>> =
        repository.getClientWorkoutsForMonth(param.id, param.yearMonth)
}