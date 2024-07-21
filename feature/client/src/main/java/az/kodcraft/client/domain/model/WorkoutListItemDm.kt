package az.kodcraft.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutListItemDm(val id:String, val name:String):Parcelable{
    companion object{
        val EMPTY = WorkoutListItemDm("", "")
    }
}
