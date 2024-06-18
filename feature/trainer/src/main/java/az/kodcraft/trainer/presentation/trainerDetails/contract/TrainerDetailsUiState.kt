package az.kodcraft.trainer.presentation.trainerDetails.contract

import android.os.Parcelable
import az.kodcraft.trainer.domain.model.TrainerDm
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerDetailsUiState(
    val trainer: TrainerDm = TrainerDm.MOCK,
    val isLoading: Boolean = false,
    val isSendRequestLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) : Parcelable {
    sealed class PartialState {
        data class Error(val message: String) : PartialState()
        data object Loading : PartialState()
        data object LoadingSendRequest : PartialState()
        data object SendRequestComplete : PartialState()
        data object UnSendRequestComplete : PartialState()
        data class SearchText(val value: String) : PartialState()
        data class Trainer(val value: TrainerDm) : PartialState()
    }


}
