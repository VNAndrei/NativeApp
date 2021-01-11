package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.remote

import retrofit2.http.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Api
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result

object ShelfApi {
    interface Service {
        @GET("/api/beers")
        suspend fun find(): List<Beer>

        @Headers("Content-Type: application/json")
        @POST("/api/beers")
        suspend fun create(@Body beer: Beer): Beer

        @Headers("Content-Type: application/json")
        @PUT("/api/beers/{id}")
        suspend fun update(@Path("id") beerId: String, @Body beer: Beer): Beer
    }

    private val service: Service = Api.retrofit.create(Service::class.java)

    suspend fun getAll(): Result<List<Beer>> {
        try {
            return Result.Success(service.find())
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
    suspend fun save(beer:Beer): Result<Beer> {
        try {
            return Result.Success(service.create(beer))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
    suspend fun update(id:String,beer:Beer): Result<Beer> {
        try {
            return Result.Success(service.update(id,beer))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

}