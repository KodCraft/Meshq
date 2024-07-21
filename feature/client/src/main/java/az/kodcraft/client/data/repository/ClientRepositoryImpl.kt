package az.kodcraft.client.data.repository

import az.kodcraft.client.data.service.ClientService
import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.model.ClientDm.WorkoutSessionDm
import az.kodcraft.client.domain.model.ClientListItemDm
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.core.data.ResponseHandler
import az.kodcraft.core.domain.UserManager
import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

class ClientRepositoryImpl(private val service: ClientService) : ClientRepository {
    override suspend fun getClients(searchText: String): Flow<Response<List<ClientListItemDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { client -> client.toDm() } },
            firestoreCall = {
                service.getClients(UserManager.getUserId(), searchText)
            }
        )
    }

    override suspend fun getClientDetails(id: String): Flow<Response<ClientDm>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it?.toDm() ?: ClientDm.EMPTY },
            firestoreCall = {
                service.getClientDetails(UserManager.getUserId(), id, YearMonth.now())
            }
        )
    }

    override suspend fun getClientWorkoutsForMonth(
        id: String,
        yearMonth: YearMonth
    ): Flow<Response<List<WorkoutSessionDm>>> {
        return ResponseHandler.safeFirestoreCall(
            mapToDomain = { it.map { s -> s.toDm() } },
            firestoreCall = {
                service.getClientWorkouts(UserManager.getUserId(), id, yearMonth)
            }
        )
    }

}