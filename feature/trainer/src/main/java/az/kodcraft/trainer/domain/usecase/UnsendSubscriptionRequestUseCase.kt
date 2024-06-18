package az.kodcraft.trainer.domain.usecase

import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.trainer.domain.repository.TrainerRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UnsendSubscriptionRequestUseCase @Inject constructor(private val repository: TrainerRepository) :
    BaseUseCase<String, Response<Boolean>> {
    override suspend fun execute(param: String): Flow<Response<Boolean>> =
        repository.unsendSubscriptionRequest(trainerId = param, traineeId = UserManager.getUserId())
}