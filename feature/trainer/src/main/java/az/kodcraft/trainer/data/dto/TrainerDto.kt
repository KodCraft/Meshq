package az.kodcraft.trainer.data.dto

import az.kodcraft.trainer.domain.model.TrainerListItemDm

data class TrainerDto(
    val username: String = "",
    val uid: String = "",
    val name: String = "",
    val bio: String = "",
    val imageUrl: String = "",
){

    fun toDm() = TrainerListItemDm(id = uid, username = username, description = bio, imageUrl = imageUrl)
}
