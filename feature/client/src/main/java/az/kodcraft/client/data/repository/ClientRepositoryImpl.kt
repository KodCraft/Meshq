package az.kodcraft.client.data.repository

import az.kodcraft.client.data.service.ClientService
import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.core.data.ResponseHandler
import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.flow.Flow

class ClientRepositoryImpl (private val service: ClientService) : ClientRepository {
    override suspend fun getClients(searchText: String): Flow<Response<List<ClientListItemDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { client -> client.toDm() } },
            firestoreCall = {
                service.getClients("yY4dVHaBgvBjZWE4Aq9z",searchText)
            }
        )
    }

}