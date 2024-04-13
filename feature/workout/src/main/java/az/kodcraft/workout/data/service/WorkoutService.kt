package az.kodcraft.workout.data.service

import android.util.Log
import az.kodcraft.workout.data.dto.WorkoutDto
import az.kodcraft.workout.domain.model.WorkoutDm
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class WorkoutService(
    private val workoutRef: CollectionReference,
    private val finishedWorkoutRef: CollectionReference
) {

    suspend fun getWorkout(workoutId: String): WorkoutDto? {
        return workoutRef.document(workoutId).get().await().toObject(WorkoutDto::class.java)
            ?.apply {
                Log.d("WORKOUT_SERVICE", "isFinished: $isFinished")
                id = workoutId
                val exercisesQuerySnapshot =
                    workoutRef.document(workoutId).collection("exercises").orderBy("order").get()
                        .await()

                val exercises = exercisesQuerySnapshot.map { document ->
                    document.toObject(WorkoutDto.Exercise::class.java).apply {
                        id = document.id  // Set the document ID
                    }
                }
                if (exercises.any()) {
                    this.exercises = exercises
                }
            }
    }

    suspend fun saveFinishedWorkout(workout: WorkoutDm): Boolean {
        try {
            // Convert WorkoutDm to a map or suitable object for Firestore
            val workoutData = mapOf(
                "id" to workout.id,
                "date" to workout.date,
                "exercises" to workout.exercises.map { exercise ->
                    mapOf(
                        "id" to exercise.id,
                        "name" to exercise.name,
                        "sets" to exercise.sets
                    )
                }
            )

            // Start a Firestore transaction to ensure atomic operations
            Firebase.firestore.runTransaction { transaction ->
                // Add the workout data to the finished workouts collection
                val finishedWorkoutDocRef = finishedWorkoutRef.document()
                transaction.set(finishedWorkoutDocRef, workoutData)

                // Update the original workout document to mark it as finished
                val workoutDocRef = workoutRef.document(workout.id)
                transaction.update(workoutDocRef, "isFinished", true)

                // Log success or handle transaction completion
                println("Workout saved successfully and marked as finished")
            }.await()
            return true

        } catch (e: Exception) {
            println("Error saving workout: ${e.message}")
            throw e
        }
    }
}

