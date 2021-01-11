package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beers")
data class Beer(
    @PrimaryKey @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "abv") var abv: Double,
    @ColumnInfo(name = "type") var type: String,
    @ColumnInfo(name = "userId") var userId: String
) {
}