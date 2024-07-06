package az.kodcraft.trainer.data.dto

import az.kodcraft.trainer.domain.model.TrainerDm
import az.kodcraft.trainer.domain.model.TrainerDm.TrainerStatsDm

data class TrainerDetailsDto(
    val uid: String,
    val username: String = "",
    val bio: String = "",
    val imageUrl: String = "",
    val stats: TrainerStatsDto? = null,
    val isRequestSent: Boolean = false,
    val isSubscribed: Boolean = false,
) {

    data class TrainerStatsDto(
        val averageRating: Double,
        val experienceYears: Double,
        val studentsCount: Int
    ) {
        fun toDm() = TrainerStatsDm(
            averageRating = averageRating,
            experienceYears = experienceYears,
            studentsCount = studentsCount

        )
    }


    fun toDm() = TrainerDm(
        id = uid,
        username = username,
        bio = bio,
        imageUrl = imageUrl,
        stats = stats?.toDm() ?: TrainerStatsDm.EMPTY,
        subState = if (isSubscribed) TrainerDm.SubStatus.SUBSCRIBED else if (isRequestSent)
            TrainerDm.SubStatus.REQUEST_SENT else TrainerDm.SubStatus.NONE,
    )
}
