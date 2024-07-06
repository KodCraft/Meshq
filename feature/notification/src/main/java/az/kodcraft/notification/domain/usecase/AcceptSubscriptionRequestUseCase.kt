package az.kodcraft.notification.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.notification.domain.repository.NotificationRepository
import com.solid.copilot.network.base.usecase.BaseUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AcceptSubscriptionRequestUseCase @Inject constructor(private val repository: NotificationRepository) :
    BaseUseCase<Pair<String, String>, Response<Boolean>> {
    override suspend fun execute(param: Pair<String, String>): Flow<Response<Boolean>> =
        repository.acceptSubscriptionRequest(notificationId = param.first, subRequestId = param.second)
}