package az.kodcraft.client.data.dto

import az.kodcraft.client.domain.model.ClientDm
import az.kodcraft.client.domain.model.ClientDm.WorkoutSessionDm
import az.kodcraft.core.utils.toLocalDate
import com.google.firebase.Timestamp

data class ClientDetailsDto(
    var id:String,
    val profilePictureUrl: String = "",
    val fullName: String,
    val username: String,
    val workoutSchedule: List<WorkoutSession>
){
    data class WorkoutSession(
        val date: Timestamp,
        val workoutName: String,
        val isCompleted: Boolean
    ){
        fun toDm() = WorkoutSessionDm(date = date.toLocalDate(), workoutName = workoutName, isCompleted = isCompleted)
    }
    fun toDm() = ClientDm(id = id, name = fullName, username = username, profilePicUrl = profilePictureUrl, workoutSchedule = workoutSchedule.map { it.toDm() })
}


