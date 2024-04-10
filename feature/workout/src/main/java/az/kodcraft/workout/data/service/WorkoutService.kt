package az.kodcraft.workout.data.service

import az.kodcraft.workout.data.dto.WorkoutDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class WorkoutService(private val workoutRef: CollectionReference) {

    suspend fun getWorkout(workoutId: String): WorkoutDto? {
        return workoutRef.document(workoutId).get().await().toObject(WorkoutDto::class.java)?.apply {
            val exercisesQuerySnapshot =
                workoutRef.document(workoutId).collection("exercises").orderBy("order").get().await()
            val exercises = exercisesQuerySnapshot.toObjects(WorkoutDto.Exercise::class.java)
            if (exercises.any()) {
                this.exercises = exercises
            }
        }
    }
}

