package az.kodcraft.dashboard.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DashboardWeekWorkoutDm(
    val id: String,
    val title: String,
    val date: String,
    val content: String,
    val labels: List<String> = emptyList(),
    val isSelected:Boolean = false
) :Parcelable{
    companion object{
        val MOCK = DashboardWeekWorkoutDm(
            id = "1",
            title = "Pull",
            content = "1 arm lat pull - 2 warmup | 2 x 6-8 | 90 sec rest between arms\n" +
                    "\n" +
                    "cable 1 arm lat row - 1 warmup | 2 x 6-8 | 90 sec rest between arms\n" +
                    "\n" +
                    "db incline bench row - 1 warmup | 1 x 6-8 | 120 sec rest\n",
            date = "16 Feb",
            isSelected = false
        )
        val MOCK2 = DashboardWeekWorkoutDm(
            id = "2",
            title = "Push and Glutes",
            content = "cable standing kickback - 2 warmup | 2 x 8-10 | 90 sec rest between legs\n" +
                    "\n" +
                    "single glute bridge (on leg ext or leg curl) - 1 warmup | 1 x 8-10\n" +
                    "\n" +
                    "glute extension - 1 x 8-10 | 90 sec rest between legs\n",
            date = "16 Feb",
            isSelected = true
        )
    }
}
