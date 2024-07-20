package az.kodcraft.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class AssignWorkoutReqDm(val date: LocalDate, val traineeId:String, val workout: WorkoutListItemDm) : Parcelable {
    companion object {
        val EMPTY = AssignWorkoutReqDm(LocalDate.now(), "", WorkoutListItemDm.EMPTY)
    }
}