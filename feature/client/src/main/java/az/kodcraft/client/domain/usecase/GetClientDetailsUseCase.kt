package az.kodcraft.client.domain.usecase

import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.core.domain.bases.model.Response
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientDetailsUseCase @Inject constructor(private val repository: ClientRepository) :
    BaseUseCase<String, Response<ClientDm>> {
    override suspend fun execute(param:String): Flow<Response<ClientDm>> =
        repository.getClientDetails(param)
}