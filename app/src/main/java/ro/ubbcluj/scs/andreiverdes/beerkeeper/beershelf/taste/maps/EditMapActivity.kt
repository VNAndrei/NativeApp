package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.taste.maps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R

/**
 * This shows how to listen to some [GoogleMap] events.
 */
class EditMapActivity : AppCompatActivity(), OnMapClickListener,
    OnMapLongClickListener, OnCameraIdleListener, OnMapReadyCallback {
    companion object {
        const val LAT = "lat"
        const val LNG = "lng"

        fun newInstance(context: Context) = Intent(context, EditMapActivity::class.java)
    }
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        // return early if the map was not initialised properly
        map = googleMap ?: return
        map.setOnMapClickListener(this)
        map.setOnMapLongClickListener(this)
        map.setOnCameraIdleListener(this)
    }

    override fun onMapClick(point: LatLng) {

    }

    override fun onMapLongClick(point: LatLng) {
        returnResult(point)
    }

    override fun onCameraIdle() {
        if(!::map.isInitialized) return

    }

    private fun returnResult(point: LatLng) {
        val data = Intent().apply {
            putExtra(LAT, point.latitude)
            putExtra(LNG, point.longitude)
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}