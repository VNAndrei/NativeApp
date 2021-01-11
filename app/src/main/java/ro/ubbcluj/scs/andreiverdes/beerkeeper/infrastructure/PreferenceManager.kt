package ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure

import android.content.SharedPreferences

import com.google.gson.Gson
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.AuthRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.User


object PreferenceManager {
    private var preferences: SharedPreferences? = null



    fun initPreferences(preferences: SharedPreferences) {
        this.preferences = preferences
        AuthRepository.user=getUser()
    }

    fun setUser(user: User) {
        val g = Gson()
        with(preferences?.edit()) {
            this?.putString(R.string.user_pref_key.toString(), g.toJson(user))
            this?.apply()
        }
    }

    fun removeUser() {
        with(preferences?.edit()) {
            this?.remove(R.string.user_pref_key.toString())
            this?.apply()
        }
    }

    fun getUser(): User? {
        val userString = preferences?.getString(R.string.user_pref_key.toString(), null)
        val g = Gson()
        if (userString != null)
            return g.fromJson(userString, User::class.java)
        return null
    }
}