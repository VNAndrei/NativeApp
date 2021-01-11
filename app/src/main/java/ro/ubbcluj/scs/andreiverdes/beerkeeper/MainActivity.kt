package ro.ubbcluj.scs.andreiverdes.beerkeeper

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.ConnectivityLiveData
import ro.ubbcluj.scs.andreiverdes.beerkeeper.infrastructure.PreferenceManager

class MainActivity : AppCompatActivity() {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectivityLiveData: ConnectivityLiveData

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.initPreferences(this.getPreferences(Context.MODE_PRIVATE))
        setupActionBarWithNavController(findNavController(R.id.fragment))
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        connectivityLiveData = ConnectivityLiveData(connectivityManager)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onStart() {
        super.onStart()
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = connectivityManager.activeNetwork
        return network != null
    }

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            runOnUiThread {
                networkTxt.text = "Online"
                networkTxt.setTextColor(Color.GREEN)
            }
        }


        override fun onLost(network: Network) {
            runOnUiThread {
                networkTxt.text = "Offline"
                networkTxt.setTextColor(Color.RED)
            }
        }
    }
}




