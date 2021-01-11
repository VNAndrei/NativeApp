package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerDetails

@Database(entities = [Beer::class,BeerDetails::class], version = 1)
abstract class BeerBase : RoomDatabase() {
    abstract fun beerDao(): BeerDao
    abstract fun beerDetailsDao():BeerDetailsDao
    companion object {
        @Volatile
        private var INSTANCE: BeerBase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): BeerBase {
            val inst = INSTANCE
            if (inst != null)
                return inst
            val instance =
                Room.databaseBuilder(context.applicationContext, BeerBase::class.java, "beerDb").build()
            INSTANCE = instance
            return instance
        }
    }
}