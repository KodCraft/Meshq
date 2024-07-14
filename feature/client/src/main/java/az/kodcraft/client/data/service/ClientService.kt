package az.kodcraft.client.data.service

import az.kodcraft.client.data.dto.ClientDetailsDto
import az.kodcraft.client.data.dto.ClientDto
import az.kodcraft.core.utils.localDateToTimestamp
import az.kodcraft.workout.data.dto.AssignedWorkoutDto
import com.google.firebase.Timestamp
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
            .whereEqualTo("trainer_id", trainerId)
            .get()
            .await()
            .documents

        // Step 2: Get the trainee ids from the client documents
        val traineeIds = clientDocs.mapNotNull { it.getString("trainee_id") }

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
            .whereEqualTo("trainer_id", trainerId)
            .whereEqualTo("trainee_id", traineeId)
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
            val workoutName = workoutDoc.getString("workoutName") ?: return@mapNotNull null

            ClientDetailsDto.WorkoutSession(
                date = date,
                workoutName = workoutName,
                isCompleted = isCompleted
            )
        }

    }


    suspend fun assignWorkout(
        trainerId: String,
        traineeId: String,
        workoutId: String,
        date: Timestamp
    ): Boolean {
        val workoutDoc = workoutsRef.document(workoutId).get().await()
        val title = workoutDoc.getString("title") ?: return false
        val notes = workoutDoc.getString("notes") ?: ""
        val labels = workoutDoc.get("labels") as? List<String> ?: emptyList()
        val exercises = workoutDoc.get("exercises") as? List<Map<String, Any>> ?: emptyList()

        // Map exercises to AssignedWorkoutDto.Exercise
        val mappedExercises = exercises.map { exercise ->
            val exerciseId = exercise["id"] as? String ?: ""
            val exerciseRefId = exercise["exercise_id"] as? String ?: ""
            val name = exercise["name"] as? String ?: ""
            val sets = (exercise["sets"] as? List<Map<String, Any>>)?.map { set ->
                AssignedWorkoutDto.Exercise.Set(
                    id = set["id"] as? String ?: "",
                    type = set["type"] as? String ?: "",
                    reps = set["reps"] as? String ?: "",
                    restSeconds = (set["rest_seconds"] as? Long ?: 0L).toInt(),
                    weight = set["weight"] as? String ?: "",
                    unit = set["weight_unit"] as? String ?: "kg",
                    isComplete = set["isComplete"] as? Boolean ?: false
                )
            } ?: emptyList()

            AssignedWorkoutDto.Exercise(
                id = exerciseId,
                exerciseRefId = exerciseRefId,
                name = name,
                sets = sets
            )
        }

        // Step 2: Create a copy of the workout data in the assigned workouts collection
        val assignedWorkout = AssignedWorkoutDto(
            title = title,
            notes = notes,
            isFinished = false,
            date = date,
            labels = labels,
            exercises = mappedExercises,
            trainerId = trainerId,
            traineeId = traineeId
        )
        assignedWorkoutsRef.add(assignedWorkout).await()
        return true
    }
}