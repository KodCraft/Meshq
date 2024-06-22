package az.kodcraft.notification.data.service

import az.kodcraft.core.domain.UserManager
import az.kodcraft.notification.data.dto.NotificationDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class NotificationService(
    private val trainerSubscriptionsRef: CollectionReference,
    private val subscriptionRequestsRef: CollectionReference,
    private val notificationsRef: CollectionReference
) {
    suspend fun getNotifications(): List<NotificationDto> {
        val querySnapshot = notificationsRef
            .whereEqualTo("trainer_id", UserManager.getUserId())
            .get()
            .await()

        return querySnapshot.documents.map { document ->
            val notification = document.toObject<NotificationDto>()
            notification?.copy(id = document.id) ?: NotificationDto(id = document.id)
        }
    }

    suspend fun acceptSubscriptionRequest(notificationId: String, subRequestId: String) {

        // Fetch the subscription request
        val subRequestDoc = subscriptionRequestsRef.document(subRequestId).get().await()
        val traineeId =
            subRequestDoc.getString("trainee_id") ?: throw Exception("Trainee ID not found")
        val trainerId = UserManager.getUserId()

        // Check if the subscription already exists
        val existingSubscription = trainerSubscriptionsRef
            .whereEqualTo("trainee_id", traineeId)
            .whereEqualTo("trainer_id", trainerId)
            .get()
            .await()
            .documents
            .isNotEmpty()

        if (!existingSubscription) {
            // Add new subscription to trainer_subscriptions_collection
            val newSubscription = mapOf(
                "trainee_id" to traineeId,
                "trainer_id" to trainerId,
                "date" to Timestamp.now()
            )
            val a = trainerSubscriptionsRef.add(newSubscription).await()
            println(a.id)
        }

        // Delete the notification
        deleteNotification(notificationId)

    }

    suspend fun deleteNotification(notificationId: String) {

        // Fetch the notification
        val notificationDoc = notificationsRef.document(notificationId).get().await()
        val subRequestId = notificationDoc.getString("subscription_request_id")

        // Delete the subscription if it exists
        if (!subRequestId.isNullOrEmpty()) {
            // Delete the subscription request
            subscriptionRequestsRef.document(subRequestId).delete().await()
        }

        // Delete the notification
        notificationsRef.document(notificationId).delete().await()

    }
}