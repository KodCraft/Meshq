package az.kodcraft.trainer.domain.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import az.kodcraft.core.presentation.theme.PrimaryTurq
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerDm(
    val id: String,
    val username: String,
    val bio: String,
    val imageUrl: String,
    val stats: TrainerStatsDm,
    val subState: SubStatus = SubStatus.NONE
) : Parcelable {
    companion object {
        val EMPTY = TrainerDm("", "", "", "", TrainerStatsDm.EMPTY)
        val MOCK = TrainerDm(
            id = "123",
            "firuza.alee",
            "Firuza Aliyeva - fitness and calisthenics athlete join me to explore and extend abilities of your body",
            "https://firebasestorage.googleapis.com/v0/b/meshq-37141.appspot.com/o/IMG_8406.jpeg?alt=media&token=f3b1c402-8e77-48df-a10d-99e530c30305",
            TrainerStatsDm.MOCK
        )
    }

    @Parcelize
    data class TrainerStatsDm(
        val averageRating: Double,
        val experienceYears: Double,
        val studentsCount: Int
    ) : Parcelable {
        companion object {
            val EMPTY = TrainerStatsDm(0.0, 0.0, 0)
            val MOCK = TrainerStatsDm(4.8, 3.5, 5)
        }
    }

    enum class SubStatus(val btnTitle: String, val color: Color, val icon:Int) {
        REQUEST_SENT("Unsend request", Color.LightGray.copy(0.7f), az.kodcraft.core.R.drawable.ic_done),
        SUBSCRIBED("Unsubscribe", Color.LightGray.copy(0.7f), az.kodcraft.core.R.drawable.ic_done),
        NONE("Send request", PrimaryTurq.copy(0.7f), az.kodcraft.core.R.drawable.ic_add_circle)
    }
}

