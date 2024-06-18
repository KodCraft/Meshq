package az.kodcraft.trainer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerDm(
    val id: String,
    val username: String,
    val bio: String,
    val imageUrl: String,
    val stats: TrainerStatsDm,
    val isRequestSent: Boolean = false,
) : Parcelable {
    companion object {
        val EMPTY = TrainerDm("","", "", "", TrainerStatsDm.EMPTY)
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
}
