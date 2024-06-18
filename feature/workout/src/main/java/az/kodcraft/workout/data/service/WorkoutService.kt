package az.kodcraft.workout.data.service

import android.util.Log
import az.kodcraft.workout.data.dto.WorkoutDto
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.tasks.await

class WorkoutService(
    private val workoutRef: CollectionReference,
    private val exerciseRef: CollectionReference
) {
    suspend fun getWorkout(workoutId: String): WorkoutDto? {
        return workoutRef.document(workoutId).get().await().toObject(WorkoutDto::class.java)
            ?.apply {
                id = workoutId
                val exercisesQuerySnapshot =
                    workoutRef.document(workoutId).collection("exercises").orderBy("order")
                        .get().await()

                val exercises = exercisesQuerySnapshot.map { document ->
                    document.toObject(WorkoutDto.Exercise::class.java).apply {
                        id = document.id  // Set the document ID
                        val setsSnapshot =
                            workoutRef.document(workoutId).collection("exercises")
                                .document(document.id).collection("sets").orderBy("order").get()
                                .await()
                        sets = setsSnapshot.map { setDoc ->
                            setDoc.toObject(WorkoutDto.Exercise.Set::class.java).apply {
                                id = setDoc.id  // Set the set ID
                            }
                        }
                    }
                }
                if (exercises.any()) {
                    this.exercises = exercises
                }
            }
    }

    suspend fun saveWorkout(workout: WorkoutDto): Boolean {
        // If workout ID is empty, generate a new document ID
        if (workout.id.isEmpty()) {
            workout.id = workoutRef.document().id
        }
        val workoutDoc = workoutRef.document(workout.id)
        return try {
            workoutDoc.set(workout).await()
            workout.exercises.forEach { exercise ->
                val exerciseDoc = workoutDoc.collection("exercises").document(exercise.id)
                exerciseDoc.set(exercise).await()
                exercise.sets.forEach { set ->
                    val setDoc = exerciseDoc.collection("sets").document(set.id)
                    setDoc.set(set).await()
                }
            }
            true
        } catch (e: Exception) {
            Log.e("SAVE_WORKOUT_ERROR", "Error saving workout", e)
            false
        }
    }
}

