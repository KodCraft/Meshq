package az.kodcraft.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutsFilterReqDm(val search: String, val tags: List<WorkoutTagDm>) : Parcelable {
    companion object {
        val EMPTY = WorkoutsFilterReqDm("", emptyList())
        val MOCK = WorkoutsFilterReqDm("", listOf(WorkoutTagDm("1", "Push"),WorkoutTagDm("2", "Pull"), WorkoutTagDm("3", "Legs")))
    }
    @Parcelize
    data class WorkoutTagDm(val id:String, val name:String, val isSelected:Boolean = false):Parcelable
}