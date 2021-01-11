package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerDetails

@Dao
interface BeerDetailsDao {
    @Query("SELECT * FROM beerDetails")
    fun getAll():LiveData<List<BeerDetails>>

    @Query("SELECT * FROM beerDetails WHERE _id=:id ")
    fun getById(id: String): LiveData<BeerDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details: BeerDetails)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(details: BeerDetails)

    @Query("DELETE FROM beerDetails")
    suspend fun deleteAll()
}