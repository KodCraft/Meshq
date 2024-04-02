package az.kodcraft.workout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDm(val id: String, val title: String, val content: String) : Parcelable {
    companion object {
        val EMPTY = WorkoutDm(id = "", title = "", content = "")
        val MOCK = WorkoutDm(
            id = "",
            title = "Push and Glutes",
            content = "Cable Standing Kickback - 2 warmup | 2 x 8-10 | 90 sec rest between legs\n" +
                    "\n" +
                    "Single Glute Bridge (on leg ext or leg curl) - 1 warmup | 1 x 8-10\n" +
                    "\n" +
                    "Glute Extension - 1 x 8-10 | 90 sec rest between legs\n" +
                    "\n" +
                    "Stiff-leg Deadlift - 3 warmup | 1 x 5-7 \n" +
                    "\n" +
                    "Seated DB Press - 2 warmup | 1 x 8-10\n" +
                    "\n" +
                    "Single Arm Cable Lateral - 1 warmup | 2 x 10-12 | 60 sec rest between arms\n" +
                    "\n" +
                    "Dual Rope Tricep Extension - 2 warmup | 2 x 10-12 | 2 min rest\n"
        )
    }
}
