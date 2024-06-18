package az.kodcraft.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientListItemDm(
    val id: String,
    val name: String,
    val traineeId: String
):Parcelable{
    companion object{
        val MOCK = ClientListItemDm(id = "123", name = "Aslan mellim", "123")
    }
}
