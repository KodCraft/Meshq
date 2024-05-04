package az.kodcraft.core.data

import android.util.Log
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
        Log.d("FIRESTORE_CALL", "Loading")
        try {
            val data = withContext(dispatcher) { firestoreCall() }
            Log.d("FIRESTORE_CALL", "Success")
            emit(Response.Success(mapToDomain(data)))
        } catch (e: Exception) {
            Log.e("FIRESTORE_CALL", "Error", e)
            emit(Response.Failure(e.message ?: "An error occurred"))
        }
    }.catch { e ->
        Log.e("FIRESTORE_CALL", "Exception", e)
        emit(Response.Failure(e.message ?: "An error occurred"))
    }.flowOn(dispatcher)

}
