package az.kodcraft.client.data.service

import az.kodcraft.client.data.dto.AssignedWorkoutDto
import az.kodcraft.client.data.dto.WorkoutListItemDto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WorkoutService(
    private val workoutRef: CollectionReference,
    private val assignedWorkoutRef: CollectionReference,
) {
    suspend fun getWorkouts(
        userId: String,
        search: String,
        tags: List<String>
    ): List<WorkoutListItemDto> {
        val querySnapshot = workoutRef
            .whereEqualTo("trainerId", userId)
            .whereGreaterThanOrEqualTo("title", search)
            .whereLessThanOrEqualTo("title", search + '\uf8ff')
            .get()
            .await()

        // Map the documents into WorkoutListItemDto
        return querySnapshot.documents.mapNotNull { document ->
            document.toObject(WorkoutListItemDto::class.java)?.apply {
                id = document.id // Assuming WorkoutListItemDto has an 'id' property
            }
        }
    }


    suspend fun assignWorkout(
        trainerId: String,
        traineeId: String,
        workoutId: String,
        date: Timestamp
    ): Boolean {
        // Fetch workout document from Firestore
        val workoutDoc = workoutRef.document(workoutId).get().await()
        val title = workoutDoc.getString("title") ?: return false
        val notes = workoutDoc.getString("notes") ?: ""
        val labels = workoutDoc.get("labels") as? List<String> ?: emptyList()
        val exercises = workoutRef.document(workoutId).collection("exercises").get().await()

        // Create the assigned workout object
        val assignedWorkout = AssignedWorkoutDto(
            title = title,
            notes = notes,
            isFinished = false,
            date = date,
            labels = labels,
            trainerId = trainerId,
            traineeId = traineeId,
            workoutId = workoutId
        )

        // Add assigned workout to Firestore and get the reference to the new document
        val assignedWorkoutDoc = assignedWorkoutRef.add(assignedWorkout).await()

        // Create a batch instance
        val batch = FirebaseFirestore.getInstance().batch()

        // Iterate over each exercise document and add it to the batch
        exercises.documents.forEachIndexed { index, exerciseDoc ->
            val exerciseId = exerciseDoc.id
            val exerciseData = exerciseDoc.data ?: return@forEachIndexed

            val exerciseDto = AssignedWorkoutDto.Exercise(
                id = exerciseId,
                exerciseRefId = exerciseData["exercise_id"] as? String ?: "",
                name = exerciseData["name"] as? String ?: "",
                order = exerciseData["order"] as? Int ?: index
            )

            // Add exercise to the exercises subcollection in the batch
            val exerciseDocRef = assignedWorkoutDoc.collection("exercises").document(exerciseId)
            batch.set(exerciseDocRef, exerciseDto)

            // Fetch sets subcollection for each exercise
            val sets = exerciseDoc.reference.collection("sets").get().await()
            sets.documents.forEachIndexed() { setIndex, setDoc ->
                val setData = setDoc.data ?: return@forEachIndexed

                val setDto = AssignedWorkoutDto.Exercise.Set(
                    id = setDoc.id,
                    type = setData["type"] as? String ?: "",
                    reps = setData["reps"] as? String ?: "",
                    restSeconds = (setData["rest_seconds"] as? Long ?: 0L).toInt(),
                    weight = setData["weight"] as? String ?: "",
                    unit = setData["weight_unit"] as? String ?: "kg",
                    isComplete = setData["isComplete"] as? Boolean ?: false,
                    order = setData["order"] as? Int ?: setIndex,
                )

                // Add each set to the sets subcollection in the batch
                val setDocRef = exerciseDocRef.collection("sets").document(setDoc.id)
                batch.set(setDocRef, setDto)
            }
        }

        // Commit the batch
        batch.commit().await()

        return true
    }

}

