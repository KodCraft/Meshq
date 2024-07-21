package az.kodcraft.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.DayOfWeek
import java.time.LocalDate

@Parcelize
data class ClientDm(
    var id: String,
    val username: String,
    val name: String,
    val profilePicUrl:String,
    val workoutSchedule: List<WorkoutSessionDm> = emptyList()
):Parcelable{
    @Parcelize
    data class WorkoutSessionDm(
        val date: LocalDate,
        val workoutName: String,
        val isCompleted: Boolean
    ):Parcelable

    companion object{
        val MOCK = ClientDm( id = "", username = "aslan.lion", name = "Aslan mellim", "123")
        val EMPTY = ClientDm(id = "123",username = "", name = "", "")
    }

    fun filterWorkoutScheduleForWeek(selectedDate: LocalDate): List<WorkoutSessionDm> {
        val startOfWeek = selectedDate.with(DayOfWeek.MONDAY)
        val endOfWeek = selectedDate.with(DayOfWeek.SUNDAY)

        return workoutSchedule.filter { workout ->
            !workout.date.isBefore(startOfWeek) && !workout.date.isAfter(endOfWeek)
        }
    }
}
