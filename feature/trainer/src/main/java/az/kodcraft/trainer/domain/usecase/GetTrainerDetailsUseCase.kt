package az.kodcraft.trainer.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.trainer.domain.model.TrainerDm
import az.kodcraft.trainer.domain.repository.TrainerRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrainerDetailsUseCase @Inject constructor(private val repository: TrainerRepository) :
    BaseUseCase<String, Response<TrainerDm>> {
    override suspend fun execute(param:String): Flow<Response<TrainerDm>> =
        repository.getTrainer(param)
}