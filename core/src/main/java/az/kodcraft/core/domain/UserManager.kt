package az.kodcraft.core.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserManager {

    data class User(val id: String, val fullName: String, val username: String)

    private var userTrainee = User("pRlzBPahNZqWM0YlzJlJ", "Aslan Aslanov", "aslan.aslanov")
    private var userTrainer = User("yY4dVHaBgvBjZWE4Aq9z", "Filusa Ali", "firuza.alee")

    private var _userRole = MutableStateFlow( UserRole.TRAINER)
    val userRole = _userRole.asStateFlow()

    fun getUserId() = getUser().id
    fun getUserFullName() = getUser().fullName
    fun getUsername() = getUser().username

    fun getUser(): User = when (userRole.value) {
        UserRole.TRAINEE -> userTrainee
        UserRole.TRAINER -> userTrainer
    }

    fun switchUserRole() {
        _userRole.value =  when (userRole.value) {
            UserRole.TRAINEE -> UserRole.TRAINER
            UserRole.TRAINER -> UserRole.TRAINEE
        }
    }


    enum class UserRole {
        TRAINER,
        TRAINEE
    }
}