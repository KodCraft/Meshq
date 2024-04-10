package az.kodcraft.core.domain.bases.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

sealed class Response<out T> {

    data class Success<out T>(val data: T) : Response<T>()
    data class Failure(val error: String?) : Response<Nothing>()
    object Loading : Response<Nothing>()
    object NetworkError : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success $data"
            is Failure -> "Failure $error"
            Loading -> "Loading"
            NetworkError -> "Network is disconnected"
        }
    }
}


fun <T> Flow<Response<T>>.doOnSuccess(action: suspend (T) -> Unit): Flow<Response<T>> =
    transform { result ->
        if (result is Response.Success) {
            action(result.data)
        }
        return@transform emit(result)
    }

fun <T> Flow<Response<T>>.doOnFailure(action: suspend (String) -> Unit): Flow<Response<T>> =
    transform { result ->
        if (result is Response.Failure) {
            action(result.error?:"")
        }
        return@transform emit(result)
    }

fun <T> Flow<Response<T>>.doOnLoading(action: suspend () -> Unit): Flow<Response<T>> =
    transform { result ->
        if (result is Response.Loading) {
            action()
        }
        return@transform emit(result)
    }


fun <T> Flow<Response<T>>.doOnNetworkError(action: suspend () -> Unit): Flow<Response<T>> =
    transform { result ->
        if (result is Response.NetworkError) {
            action()
        }
        return@transform emit(result)
    }

