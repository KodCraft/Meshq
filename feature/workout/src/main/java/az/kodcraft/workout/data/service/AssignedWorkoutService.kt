package az.kodcraft.workout.data.service

import android.util.Log
import az.kodcraft.workout.data.dto.AssignedWorkoutDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AssignedWorkoutService(
    private val assignedWorkoutRef: CollectionReference,
    private val exerciseLogRef: CollectionReference,
) {

    suspend fun getWorkout(workoutId: String): AssignedWorkoutDto? {
        return assignedWorkoutRef.document(workoutId).get().await().toObject(AssignedWorkoutDto::class.java)
            ?.apply {
                id = workoutId
                val exercisesQuerySnapshot =
                    assignedWorkoutRef.document(workoutId).collection("exercises").orderBy("order")
                        .get()
                        .await()

                val exercises = exercisesQuerySnapshot.map { document ->
                    document.toObject(AssignedWorkoutDto.Exercise::class.java).apply {
                        id = document.id  // Set the document ID
                        val setsSnapshot =
                            assignedWorkoutRef.document(workoutId).collection("exercises")
                                .document(document.id).collection("sets").orderBy("order").get()
                                .await()
                        sets = setsSnapshot.map { setDoc ->
                            setDoc.toObject(AssignedWorkoutDto.Exercise.Set::class.java).apply {

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

    suspend fun saveFinishedWorkout(assignedWorkout: AssignedWorkoutDto): Boolean {
        val workoutDoc = assignedWorkoutRef.document(assignedWorkout.id)

        val existingExerciseLogs =
            exerciseLogRef.whereEqualTo("assignedWorkout", workoutDoc).get()
                .await()
        FirebaseFirestore.getInstance().runTransaction { transaction ->
            // Update the 'isFinished' field of the workout
            transaction.update(workoutDoc, "isFinished", assignedWorkout.isFinished)

            // Delete exercise logs associated with the assigned workout
            existingExerciseLogs.documents.forEach { doc ->
                transaction.delete(doc.reference)
            }

            // Update the 'weight' in each exercise's set and 'isComplete' status
            assignedWorkout.exercises.forEach { exercise ->
                exercise.sets.forEach { set ->
                    val setDocRef = workoutDoc
                        .collection("exercises").document(exercise.id)
                        .collection("sets").document(set.id)

                    // Update 'isComplete' and 'weight' for each set within a single transaction
                    transaction.update(setDocRef, "isComplete", set.isComplete)
                    transaction.update(setDocRef, "weight", set.weight)
                }
                exercise.sets.lastOrNull { it.type == "working" && it.weight.isNotBlank() }
                    ?.let { set ->

                        val assignedWorkoutRef = assignedWorkoutRef.document(assignedWorkout.id)
                        val exerciseLog = hashMapOf(
                            "userId" to "123",
                            "assignedWorkout" to assignedWorkoutRef,
                            "exercise_id" to exercise.exerciseRefId,
                            "weight" to set.weight,
                            "timestamp" to FieldValue.serverTimestamp()
                            // Add more fields as needed
                        )

                        transaction.set(exerciseLogRef.document(), exerciseLog)
                    }
            }
        }.await()
        return true
    }


}

