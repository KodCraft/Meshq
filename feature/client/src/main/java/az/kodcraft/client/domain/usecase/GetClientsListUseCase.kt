package az.kodcraft.client.domain.usecase

import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.core.domain.bases.model.Response
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientsListUseCase @Inject constructor(private val repository: ClientRepository) :
    BaseUseCase<String, Response<List<ClientListItemDm>>> {
    override suspend fun execute(param:String): Flow<Response<List<ClientListItemDm>>> =
        repository.getClients(param)
}