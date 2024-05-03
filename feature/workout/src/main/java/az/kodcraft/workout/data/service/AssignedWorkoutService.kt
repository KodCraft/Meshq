package az.kodcraft.workout.data.service

import android.util.Log
import az.kodcraft.workout.data.dto.WorkoutDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssignedWorkoutService(
    private val workoutRef: CollectionReference
) {

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

                        val setsSnapshot = workoutRef.document(workoutId).collection("exercises")
                            .document(document.id).collection("sets").orderBy("order").get().await()
                        sets = setsSnapshot.map { setDoc ->
                            setDoc.toObject(WorkoutDto.Exercise.Set::class.java).apply {

                                Log.d("SET_FETCHING", setDoc.data.toString())
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

    suspend fun saveFinishedWorkout(workout: WorkoutDto): Boolean {
        val workoutDoc = workoutRef.document(workout.id)

        FirebaseFirestore.getInstance().runTransaction { transaction ->
            // Update the 'isFinished' field of the workout
            transaction.update(workoutDoc, "isFinished", workout.isFinished)

            // Update the 'weight' in each exercise's set and 'isComplete' status
            workout.exercises.forEach { exercise ->
                exercise.sets.forEach { set ->
                    val setDocRef = workoutDoc
                        .collection("exercises").document(exercise.id)
                        .collection("sets").document(set.id)

                    // Update 'isComplete' and 'weight' for each set within a single transaction
                    transaction.update(setDocRef, "isComplete", set.isComplete)
                    transaction.update(setDocRef, "weight", set.weight)
                }
            }
        }.await()
        return true
    }
}

