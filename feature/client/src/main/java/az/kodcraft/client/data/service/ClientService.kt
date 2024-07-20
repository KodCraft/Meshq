package az.kodcraft.client.data.service

import az.kodcraft.client.data.dto.ClientDetailsDto
import az.kodcraft.client.data.dto.ClientDto
import az.kodcraft.core.utils.localDateToTimestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.tasks.await
import java.time.YearMonth

class ClientService(
    private val usersRef: CollectionReference,
    private val clientsRef: CollectionReference,
    private val assignedWorkoutsRef: CollectionReference,
    private val workoutsRef: CollectionReference,
) {
    suspend fun getClients(trainerId: String, searchText: String): List<ClientDto> {
        // Step 1: Get the list of client documents for the given trainerId
        val clientDocs = clientsRef
            .whereEqualTo("trainerId", trainerId)
            .get()
            .await()
            .documents

        // Step 2: Get the trainee ids from the client documents
        val traineeIds = clientDocs.mapNotNull { it.getString("traineeId") }

        // Step 3: Get the user documents for the trainee ids
        val userDocs = usersRef
            .whereIn(FieldPath.documentId(), traineeIds)
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .get()
            .await()


        // Step 4: Map the user documents to ClientDto
        return userDocs.documents.mapNotNull { userDoc ->
            val traineeId = userDoc.id
            val name = userDoc.getString("name")
            if (name != null) {
                ClientDto(id = userDoc.id, name = name, traineeId = traineeId)
            } else {
                null
            }
        }
    }


    suspend fun getClientDetails(
        trainerId: String,
        traineeId: String,
        yearMonth: YearMonth
    ): ClientDetailsDto? {
        // Step 1: Get user details
        val userDoc = usersRef
            .document(traineeId)
            .get()
            .await()

        val profilePictureUrl = userDoc.getString("imageUrl") ?: ""
        val fullName = userDoc.getString("name") ?: return null
        val username = userDoc.getString("username") ?: return null


        // Step 3: Map assigned workouts to WorkoutSession
        val workoutSchedule = getClientWorkouts(trainerId, traineeId, yearMonth)

        // Step 4: Return the ClientDetailsDto
        return ClientDetailsDto(
            id = traineeId,
            profilePictureUrl = profilePictureUrl,
            fullName = fullName,
            username = username,
            workoutSchedule = workoutSchedule
        )
    }

    suspend fun getClientWorkouts(
        trainerId: String,
        traineeId: String,
        yearMonth: YearMonth
    ): List<ClientDetailsDto.WorkoutSession> {

        val startTimestamp = localDateToTimestamp(yearMonth.atDay(1))
        val endTimestamp = localDateToTimestamp(yearMonth.atEndOfMonth())

        val assignedWorkoutDocs = assignedWorkoutsRef
            .whereEqualTo("trainerId", trainerId)
            .whereEqualTo("traineeId", traineeId)
            .whereGreaterThanOrEqualTo("date", startTimestamp)
            .whereLessThanOrEqualTo("date", endTimestamp)
            .get()
            .await()
            .documents
        // Step 3: Map assigned workouts to WorkoutSession
        return assignedWorkoutDocs.mapNotNull { assignedWorkoutDoc ->
            val workoutId = assignedWorkoutDoc.getString("workoutId") ?: return@mapNotNull null
            val date = assignedWorkoutDoc.getTimestamp("date") ?: return@mapNotNull null
            val isCompleted = assignedWorkoutDoc.getBoolean("isCompleted") ?: false

            // Get the workout details from workouts collection
            val workoutDoc = workoutsRef.document(workoutId).get().await()
            val workoutName = workoutDoc.getString("title") ?: return@mapNotNull null

            ClientDetailsDto.WorkoutSession(
                date = date,
                workoutName = workoutName,
                isCompleted = isCompleted
            )
        }

    }

}