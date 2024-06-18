package az.kodcraft.client.domain.repository

import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    suspend fun getClients(searchText: String): Flow<Response<List<ClientListItemDm>>>
}