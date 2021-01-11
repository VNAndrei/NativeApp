package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beerDetails")
data class BeerDetails(
    @PrimaryKey @ColumnInfo(name = "_id") var _id: String,
    @ColumnInfo(name = "imgPath") var imagePath: String?,
    @ColumnInfo(name = "lat") var lat: Double?,
    @ColumnInfo(name = "lng") var lng: Double?
) {
}