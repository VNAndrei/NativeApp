package ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data

import android.preference.PreferenceManager
import android.util.Log
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.remote.AuthApi
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Api
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result


object AuthRepository {
    var user: User? = null
        set(value){
            field= value
            Api.tokenInterceptor.token = value?.token
        }

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        Api.tokenInterceptor.token = null
        ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.PreferenceManager.removeUser()
    }

    suspend fun login(username: String, password: String): Result<User> {
        val credentials = Credentials(username, password)
        val result = AuthApi.login(credentials)
        if (result is Result.Success<User>) {
            setLoggedInUser(result.data)
        }
        return result
    }

    private fun setLoggedInUser(user: User) {
        ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.PreferenceManager.setUser(user)
        this.user = user
        Api.tokenInterceptor.token = user.token
    }
}
