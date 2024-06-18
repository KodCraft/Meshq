package az.kodcraft.trainer.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.trainer.domain.model.TrainerListItemDm
import az.kodcraft.trainer.domain.repository.TrainerRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrainersListUseCase @Inject constructor(private val repository: TrainerRepository) :
    BaseUseCase<String, Response<List<TrainerListItemDm>>> {
    override suspend fun execute(param:String): Flow<Response<List<TrainerListItemDm>>> =
        repository.getTrainers(param)
}