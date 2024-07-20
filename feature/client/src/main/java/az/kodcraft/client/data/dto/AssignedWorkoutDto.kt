package az.kodcraft.client.data.dto

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class AssignedWorkoutDto(
    var workoutId: String = "",
    val title: String = "",
    val notes: String = "",
    @field:JvmField
    val isFinished: Boolean = false,
    val date: Timestamp = Timestamp.now(),
    val labels: List<String> = emptyList(),
    val trainerId: String = "",
    val traineeId: String = ""
) {

    data class Exercise(
        var id: String = "",
        @get:PropertyName("exercise_id") @set:PropertyName("exercise_id")
        var exerciseRefId: String = "",
        val name: String = "",
        var sets: List<Set> = emptyList(),
        val order: Int
    ) {
        data class Set(
            var id: String = "",
            val type: String = "",
            val reps: String = "",
            @get:PropertyName("rest_seconds")
            val restSeconds: Int = 0,
            val weight: String = "",
            @get:PropertyName("weight_unit")
            val unit: String = "kg",
            @field:JvmField
            val isComplete: Boolean = false,
            val order: Int = 0,
        )
    }

}
