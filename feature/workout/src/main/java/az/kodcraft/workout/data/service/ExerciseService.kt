package az.kodcraft.workout.data.service

import az.kodcraft.workout.data.dto.ExerciseDto
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class ExerciseService(
    private val exerciseRef: CollectionReference
) {
    suspend fun getExercises(searchText: String): List<ExerciseDto> {
        val querySnapshot = exerciseRef
            .orderBy("name")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
            .get().await()

        return querySnapshot.documents.mapNotNull { document ->
            document.toObject<ExerciseDto>()?.apply {
                id = document.id
            }
        }
    }
}