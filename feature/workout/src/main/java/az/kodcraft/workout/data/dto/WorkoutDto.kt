package az.kodcraft.workout.data.dto

import az.kodcraft.core.utils.formatDateToStringDatAndMonth
import az.kodcraft.workout.domain.model.WorkoutDm
import com.google.firebase.Timestamp

data class WorkoutDto(
    var id: String = "",
    val title: String = "",
    val notes: String = "",
    val date: Timestamp = Timestamp.now(),
    val labels: List<String> = emptyList(),
    var exercises: List<Exercise> = emptyList()
) {

    data class Exercise(
        var id: String = "",
        val name: String = "",
        val sets: List<Set> = emptyList()
    ) {
        data class Set(
            val type: String = "",
            val reps: String = "",
            val rest_seconds: Int = 0,
            val weight: String= ""
        )
    }

    fun toDm() = WorkoutDm(
        id = id,
        date = date.toDate().formatDateToStringDatAndMonth(),
        title = title,
        notes = notes,
        exercises = exercises.map {
            WorkoutDm.Exercise(id = it.id, name = it.name, sets = it.sets.map { set ->
                WorkoutDm.Exercise.Set(
                    type = set.type,
                    reps = set.reps,
                    restSeconds = set.rest_seconds,
                    weight = set.weight
                )
            })
        }
    )

}