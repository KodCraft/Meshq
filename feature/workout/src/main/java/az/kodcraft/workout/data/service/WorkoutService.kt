package az.kodcraft.workout.data.service

import az.kodcraft.workout.data.dto.WorkoutDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class WorkoutService(private val workoutRef: CollectionReference) {

    suspend fun getWorkout(workoutId: String): WorkoutDto? {
        return workoutRef.document(workoutId).get().await().toObject(WorkoutDto::class.java)
            ?.apply {
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
}

