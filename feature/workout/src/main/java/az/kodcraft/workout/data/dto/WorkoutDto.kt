package az.kodcraft.workout.data.dto

import az.kodcraft.workout.domain.model.WorkoutDm
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class WorkoutDto(
    var id: String = "",
    val title: String = "",
    val notes: String = "",
    @field:JvmField
    val isFinished: Boolean = false,
    val date: Timestamp = Timestamp.now(),
    val labels: List<String> = emptyList(),
    var exercises: List<Exercise> = emptyList()
) {

    data class Exercise(
        var id: String = "",
        @get:PropertyName("exercise_id") @set:PropertyName("exercise_id")
        var exerciseRef: DocumentReference? = null,
        val name: String = "",
        var sets: List<Set> = emptyList()
    ) {
        data class Set(
            var id:String = "",
            val type: String = "",
            val reps: String = "",
            @get:PropertyName("rest_seconds")
            val restSeconds: Int = 0,
            val weight: String= "",
            @get:PropertyName("weight_unit")
            val unit: String= "kg",
            @field:JvmField
            val isComplete: Boolean= false,
        )
    }

    fun toDm() = WorkoutDm(
        id = id,
        date = date.toDate(),
        title = title,
        isFinished = isFinished,
        notes = notes,
        exercises = exercises.map {
            WorkoutDm.Exercise(id = it.id, name = it.name, sets = it.sets.map { set ->
                WorkoutDm.Exercise.Set(
                    id = set.id,
                    type = set.type,
                    reps = set.reps,
                    restSeconds = set.restSeconds,
                    weight = set.weight,
                    unit = set.unit,
                    isComplete = set.isComplete
                )
            })
        }
    )
companion object{

    fun WorkoutDm.toDto() = WorkoutDto(
        id = id,
        date = Timestamp(date),
        title = title,
        isFinished = isFinished,
        notes = notes,
        exercises = exercises.map {
            Exercise(id = it.id, name = it.name, sets = it.sets.map { set ->
                Exercise.Set(
                    id = set.id,
                    type = set.type,
                    reps = set.reps,
                    restSeconds = set.restSeconds,
                    weight = set.weight,
                    unit = set.unit,
                    isComplete = set.isComplete
                )
            })
        }
    )
}

}