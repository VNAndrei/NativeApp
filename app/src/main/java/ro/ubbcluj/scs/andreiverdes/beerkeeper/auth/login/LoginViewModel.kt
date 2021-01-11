package ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.AuthRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.User
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result

class LoginViewModel : ViewModel() {
    val user: MutableLiveData<User> = MutableLiveData()
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result: Result<User> = AuthRepository.login(username, password)
            if (result is Result.Success<User>) {
                user.value = result.data
            }
        }
    }
}