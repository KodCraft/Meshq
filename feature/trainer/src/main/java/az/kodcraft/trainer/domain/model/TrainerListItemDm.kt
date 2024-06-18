package az.kodcraft.trainer.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainerListItemDm(val id:String, val username:String, val description: String, val imageUrl:String = ""):Parcelable{
    companion object{
        val MOCK1 = TrainerListItemDm("1","firuza.alee", "Firuza Aliyeva - fitness and calisthenics athlete")
        val MOCK2 = TrainerListItemDm("12","filankes.oglu", "Filankes Oglu - fitness | scientific approach")
        val MOCK3 = TrainerListItemDm("123","filankes.qizi", "Filankes Qizi - Yoga and streaching quru")
        val MOCK4 = TrainerListItemDm("1234","mahammad.ali", "Mahammad Ali - Box and Crossfit")
    }
}
