package ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.remote

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.Credentials
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.User
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Api
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result
import java.lang.Exception

object AuthApi{
    interface AuthService{
        @Headers("Content-Type: application/json")
        @POST("api/auth/login")
        suspend fun login(@Body credentials: Credentials): User
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(credentials: Credentials):Result<User>{
        try {
            return Result.Success(authService.login(credentials))
        }
        catch (e:Exception){
            return Result.Error(e)
        }
    }

}