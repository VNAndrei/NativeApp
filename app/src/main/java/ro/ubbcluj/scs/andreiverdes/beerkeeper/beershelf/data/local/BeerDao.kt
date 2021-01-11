package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer

@Dao
interface BeerDao {
    @Query("SELECT * FROM beers WHERE userId = :userId")
    fun getAll(userId : String):LiveData<List<Beer>>

    @Query("SELECT * FROM beers")
    fun getAll():LiveData<List<Beer>>

    @Query("SELECT * FROM beers WHERE _id=:id ")
    fun getById(id: String): LiveData<Beer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(beer: Beer)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(beer: Beer)

    @Query("DELETE FROM beers")
    suspend fun deleteAll()
}