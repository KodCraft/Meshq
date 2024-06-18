package az.kodcraft.workout.data.dto

import az.kodcraft.workout.domain.model.CreateWorkoutDm
import com.google.firebase.firestore.PropertyName

data class WorkoutDto(
    var id: String = "",
    val title: String = "",
    val notes: String = "",
    val labels: List<String> = emptyList(),
    var exercises: List<Exercise> = emptyList()
) {

    data class Exercise(
        var id: String = "",
        @get:PropertyName("exercise_id") @set:PropertyName("exercise_id")
        var exerciseRefId: String = "",
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
        )
    }

    fun toDm() = CreateWorkoutDm(
        id = id,
        title = title,
        notes = notes,
        exercises = exercises.map {
            CreateWorkoutDm.Exercise(id = it.id, name = it.name,exerciseRefId = it.exerciseRefId, sets = it.sets.map { set ->
                CreateWorkoutDm.Exercise.Set(
                    id = set.id,
                    type = set.type,
                    reps = set.reps,
                    restSeconds = set.restSeconds.toString(),
                    weight = set.weight,
                    unit = set.unit,
                )
            })
        }
    )
companion object{

    fun CreateWorkoutDm.toDto() = WorkoutDto(
        id = id,
        title = title,
        notes = notes,
        exercises = exercises.map {
            Exercise(id = it.id, name = it.name, exerciseRefId = it.exerciseRefId, sets = it.sets.map { set ->
                Exercise.Set(
                    id = set.id,
                    type = set.type,
                    reps = set.reps,
                    restSeconds = set.restSeconds.toIntOrNull()?:0,
                    weight = set.weight,
                    unit = set.unit,
                )
            })
        }
    )

}

}