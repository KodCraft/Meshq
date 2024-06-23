package az.kodcraft.trainer.data.service

import az.kodcraft.core.domain.UserManager
import az.kodcraft.trainer.data.dto.TrainerDetailsDto
import az.kodcraft.trainer.data.dto.TrainerDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class TrainerService(
    private val usersRef: CollectionReference,
    private val userStatsRef: CollectionReference,
    private val subscriptionRequestsRef: CollectionReference,
    private val notificationsRef: CollectionReference

) {
    suspend fun getTrainers( searchText: String): List<TrainerDto> {

        val querySnapshot = usersRef
            .whereEqualTo("role", "trainer")
            .orderBy("username")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { document ->
            document.toObject(TrainerDto::class.java)?.copy(uid = document.id)
        }
    }

    suspend fun getTrainer(id: String): TrainerDetailsDto? {
        val userDocumentSnapshot = usersRef.document(id).get().await()
        val trainerStatsSnapshot = userStatsRef.whereEqualTo("userId", id).get().await()

        if (!userDocumentSnapshot.exists()) {
            return null
        }

        val trainerDto = userDocumentSnapshot.toObject(TrainerDto::class.java)
        val trainerStatsDto = trainerStatsSnapshot.documents.firstOrNull()?.let { document ->
            TrainerDetailsDto.TrainerStatsDto(
                averageRating = document.getDouble("rating_average") ?: 0.0,
                experienceYears = document.getDouble("experience_years_count") ?: 0.0,
                studentsCount = document.getLong("trainees_count")?.toInt() ?: 0
            )
        }
        val subscriptionRequestSnapshot = subscriptionRequestsRef
            .whereEqualTo("trainer_id", id)
            .whereEqualTo("trainee_id", UserManager.getUserId())
            .get()
            .await()

        val isRequestSent = !subscriptionRequestSnapshot.isEmpty


        return trainerDto?.let {
            TrainerDetailsDto(
                uid = userDocumentSnapshot.id,
                username = it.username,
                bio = it.bio,
                imageUrl = it.imageUrl,
                stats = trainerStatsDto,
                isRequestSent = isRequestSent
            )
        }
    }

    suspend fun sendSubscriptionRequest(traineeId: String, trainerId: String) {
        val subscriptionRequest = hashMapOf(
            "trainer_id" to trainerId,
            "trainee_id" to traineeId,
            "date" to com.google.firebase.Timestamp.now()
        )

        subscriptionRequestsRef.add(subscriptionRequest).await()
    }

    suspend fun unSendSubscriptionRequest(traineeId: String, trainerId: String) {
        val querySnapshot = subscriptionRequestsRef
            .whereEqualTo("trainer_id", trainerId)
            .whereEqualTo("trainee_id", traineeId)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            subscriptionRequestsRef.document(document.id).delete().await()
        }


        // Check and delete corresponding notification
        val notificationSnapshot = notificationsRef
            .whereEqualTo("trainer_id", trainerId)
            .whereEqualTo("subscription_request_id", querySnapshot.documents.firstOrNull()?.id)
            .get()
            .await()

        for (document in notificationSnapshot.documents) {
            notificationsRef.document(document.id).delete().await()
        }

    }
}