package az.kodcraft.client.data.dto

import az.kodcraft.client.domain.model.ClientListItemDm

data class ClientDto(
    var id: String,
    val name: String,
    val traineeId: String
) {
    fun toDm() = ClientListItemDm(name = name, id = id, traineeId = traineeId)
}
