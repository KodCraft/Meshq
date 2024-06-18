package az.kodcraft.trainer.presentation.trainerList.contract

import android.os.Parcelable
import az.kodcraft.trainer.domain.model.TrainerListItemDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerListUiState(
    val trainerList: List<TrainerListItemDm> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val searchValue: String = "",
    val errorMessage:String = ""
) : Parcelable {
    sealed class PartialState {
        data class Error(val message : String) : PartialState()
        data object Loading : PartialState()
        data class SearchText(val value: String) : PartialState()
        data class TrainersList(val value: List<TrainerListItemDm>) : PartialState()
    }


}
