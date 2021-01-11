package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.taste

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerDetails
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.Result
class BeerViewModel(application: Application):AndroidViewModel(application) {
    var done = MutableLiveData<Boolean>(false)
    lateinit var point: LatLng
    lateinit var imagePath : String

    fun getById(id:String)=BeerRepository.getById(id)
    fun getDetails(id:String)=BeerRepository.getDetails(id)
    fun saveChanges(beer: Beer){
        viewModelScope.launch {
            val result: Result<Beer>
            if (beer._id=="") {
                result =BeerRepository.save(beer, BeerDetails("",imagePath,point.latitude,point.longitude))
            }
            else{
                result=BeerRepository.update(beer,BeerDetails(beer._id,imagePath,point.latitude,point.longitude))
            }
           when(result){
               is Result.Success->{
                    done.value= true
               }
               is Result.Error -> {
                    done.value= false
               }
           }

        }
    }
}