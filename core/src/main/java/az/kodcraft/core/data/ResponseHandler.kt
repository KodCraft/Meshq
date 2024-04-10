package az.kodcraft.core.data

import az.kodcraft.core.domain.bases.model.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

object ResponseHandler {

    suspend fun <T, D> safeFirestoreCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        mapToDomain: suspend (T) -> D,
        firestoreCall: suspend () -> T,
    ): Flow<Response<D>> = flow {
        emit(Response.Loading)
        try {
            val data = withContext(dispatcher) { firestoreCall() }
            emit(Response.Success(mapToDomain(data)))
        } catch (e: Exception) {
            emit(Response.Failure( e.message ?: "An error occurred"))
        }
    }.catch { e ->
        emit(Response.Failure( e.message ?: "An error occurred"))
    }.flowOn(dispatcher)

}
