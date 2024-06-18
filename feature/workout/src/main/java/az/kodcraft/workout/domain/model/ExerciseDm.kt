package az.kodcraft.workout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseDm(val id: String, val name: String, val note:String, val unit:String) : Parcelable{
    companion object{
        val EMPTY = ExerciseDm("", "", "", "")
    }
}
