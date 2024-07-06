package az.kodcraft.notification.domain.usecase

import az.kodcraft.core.domain.bases.model.Response
import az.kodcraft.notification.domain.model.NotificationListItemDm
import az.kodcraft.notification.domain.repository.NotificationRepository
import com.solid.network.base.usecase.BaseUseCaseNoParam
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationsListUseCase @Inject constructor(private val repository: NotificationRepository) :
    BaseUseCaseNoParam<Response<List<NotificationListItemDm>>> {
    override suspend fun execute(): Flow<Response<List<NotificationListItemDm>>> =
        repository.getNotifications()
}