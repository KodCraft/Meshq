package az.kodcraft.client.data.dto

import az.kodcraft.client.domain.model.WorkoutListItemDm

data class WorkoutListItemDto(var id: String = "", val title: String = "") {
    fun toDm() = WorkoutListItemDm(id, title)
}
