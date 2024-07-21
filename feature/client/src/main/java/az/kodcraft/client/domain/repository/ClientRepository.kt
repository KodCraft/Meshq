package az.kodcraft.client.domain.repository

import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.model.ClientDm.WorkoutSessionDm
import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface ClientRepository {
    suspend fun getClients(searchText: String): Flow<Response<List<ClientListItemDm>>>
    suspend fun getClientDetails(id: String): Flow<Response<ClientDm>>
    suspend fun getClientWorkoutsForMonth(
        id: String,
        yearMonth: YearMonth
    ): Flow<Response<List<WorkoutSessionDm>>>
}