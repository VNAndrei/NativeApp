package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data

import androidx.lifecycle.LiveData
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.AuthRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local.BeerDao
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local.BeerDetailsDao
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.remote.ShelfApi
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result

object BeerRepository {
    lateinit var beerDao: BeerDao
    lateinit var detailsDao: BeerDetailsDao
    lateinit var beers: LiveData<List<Beer>>
    lateinit var details: LiveData<List<BeerDetails>>
    var isInitialized = false
    fun initializeRepository(beerDao: BeerDao,detailsDao: BeerDetailsDao) {
        this.beerDao = beerDao
        this.detailsDao = detailsDao
        details = detailsDao.getAll()
        beers = beerDao.getAll(AuthRepository.user!!._id)
        isInitialized=true
    }

    suspend fun refresh(): Result<List<Beer>> {
        val result = ShelfApi.getAll()
        if (result is Result.Success<List<Beer>>) {
            for (beer in result.data) {
                beerDao.insert(beer)
            }
        }
        return result
    }

    fun getById(id: String): LiveData<Beer> {
        return beerDao.getById(id)
    }
    fun getDetails(id: String): LiveData<BeerDetails> {
        return detailsDao.getById(id)
    }
    suspend fun save(beer: Beer,details:BeerDetails): Result<Beer> {

        val result = ShelfApi.save(beer)
        if (result is Result.Success<Beer>){
            beerDao.insert(result.data)
            details._id = result.data._id
            detailsDao.insert(details)
        }

        return result
    }

    suspend fun update(item: Beer,details:BeerDetails): Result<Beer> {
        if(detailsDao.getById(item._id).value!=null){
            detailsDao.update(details)
        }
        else{
            detailsDao.insert(details)
        }
        val result = ShelfApi.update(item._id, item)
        if (result is Result.Success<Beer>)
            beerDao.update(result.data)
        return result
    }
}