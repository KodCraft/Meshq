package az.kodcraft.workout.data.dto

import az.kodcraft.workout.domain.model.ExerciseDm

data class ExerciseDto(
    var id: String = "",
    val name:String = "",
    val note: String = "",
    val unit:String = ""
)
fun ExerciseDto.toDm() = ExerciseDm(name = this.name, id = id, note = note, unit = unit)