package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.shelf

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerDetails
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Filter
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local.BeerBase
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result

class BeersViewModel(application: Application) : AndroidViewModel(application) {
    val beers: LiveData<List<Beer>>
    val details: LiveData<List<BeerDetails>>


    init {

        val beerDao = BeerBase.getDatabase(application, viewModelScope).beerDao()
        val detailsDao = BeerBase.getDatabase(application, viewModelScope).beerDetailsDao()
        if (!BeerRepository.isInitialized)
            BeerRepository.initializeRepository(beerDao,detailsDao)
        beers = BeerRepository.beers
        details = BeerRepository.details

    }
    fun applySearchFilter(filter: Filter,search:String):List<Beer>{
        return beers.value!!.filter { beer ->
           beer.name.contains(search,true)
        }.filter {  beer -> applyFilter(filter,beer)  }
    }
    private fun applyFilter(filter: Filter, beer:Beer):Boolean{

        var ok = false
        if(filter.ale)
            ok = ok || beer.type=="Ale"
        if(filter.lager)
            ok = ok || beer.type=="Lager"
        if(filter.hybrid)
            ok = ok || beer.type=="Hybrid"
        if (!(filter.ale||filter.hybrid||filter.lager))
            return true
        return ok
    }
    fun refresh(function: () -> Unit) {
        viewModelScope.launch {
            when (val result = BeerRepository.refresh()) {
                is Result.Success -> {
                    Log.d("da", "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w("da", "refresh failed", result.exception);
                }
            }
        }.invokeOnCompletion { function() }
    }

}