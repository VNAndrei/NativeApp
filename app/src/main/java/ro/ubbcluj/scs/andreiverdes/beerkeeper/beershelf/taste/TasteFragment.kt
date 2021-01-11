package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.taste

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import android.widget.ViewAnimator
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_taste.*
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R
import ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data.AuthRepository
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.taste.maps.EditMapActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.hypot

class TasteFragment : Fragment(), OnMapReadyCallback {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private val CAMERA_REQUEST = 1
    private val MAP_REQUEST = 2
    private val REQUEST_PERMISSION_CAMERA = 10
    private val REQUEST_PERMISSION_STORAGE = 11
    private val REQUEST_PICK_IMAGE = 3
    lateinit var currentPhotoPath: String

    private lateinit var viewModel: BeerViewModel
    private var beerId: String = ""
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                beerId = it.getString(ITEM_ID).toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
        checkManageExternalStoragePermission()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map.onCreate(null)
        map.onResume()
        map.getMapAsync(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_taste, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFragment()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                val uri = Uri.parse(currentPhotoPath)
                image.setImageURI(uri)
                image.visibility = View.VISIBLE
                viewModel.imagePath = currentPhotoPath

            } else if (requestCode == MAP_REQUEST) {
                revealView(map)
                data?.apply {
                    val lat = getDoubleExtra(EditMapActivity.LAT, 0.0)
                    val lng = getDoubleExtra(EditMapActivity.LNG, 0.0)
                    val point = LatLng(lat, lng)
                    gMap.clear()
                    gMap.addMarker(MarkerOptions().position(point))
                    gMap.moveCamera(CameraUpdateFactory.newLatLng(point))
                    viewModel.point = point
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                val uri = data?.data

                image.setImageURI(uri)
                val drawable: BitmapDrawable = image.drawable as BitmapDrawable
                val bitmap: Bitmap = drawable.bitmap
                revealView(image)
                saveImage(bitmap)
                viewModel.imagePath = currentPhotoPath
            }
        }

    }

    override fun onMapReady(readyMap: GoogleMap) {
        MapsInitializer.initialize(context)
        gMap = readyMap
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupFragment() {
        viewModel = ViewModelProvider(this).get(BeerViewModel::class.java)
        initializeViewModelObservers()
        initializeTasteButton()
        initializeLocationButton()
        initializeImageButton()
        initializeGalleryButton()
    }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_PERMISSION_CAMERA
                )
            }
        }
    }


    private fun checkManageExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION_STORAGE
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initializeViewModelObservers() {
        if (beerId != "") {
            viewModel.getById(beerId!!).observe(viewLifecycleOwner) { beer ->
                when (beer.type) {
                    "Ale" -> {
                        aleButton.isChecked = true;
                    }
                    "Lager" -> {
                        lagerButton.isChecked = true;
                    }
                    "Hybrid" -> {
                        hybridButton.isChecked = true;
                    }
                }
                name.setText(beer.name)
                abv.setText("${beer.abv}")

            }

            viewModel.getDetails(beerId).observe(viewLifecycleOwner) { details ->
                if (details != null) {
                    if (details.imagePath != null) {
                        revealView(image)
                        image.setImageURI(Uri.parse(details.imagePath))
                        viewModel.imagePath = details.imagePath!!
                    }
                    if (details.lat != null && details.lng != null) {
                        revealView(map)
                        val point = LatLng(details.lat!!, details.lng!!)
                        gMap.clear()
                        gMap.addMarker(MarkerOptions().position(point))
                        gMap.moveCamera(CameraUpdateFactory.newLatLng(point))
                        viewModel.point = point
                    }
                }
            }

        }
        viewModel.done.observe(viewLifecycleOwner) {
            if (it)
                findNavController().popBackStack()
        }
    }


    private fun initializeImageButton() {
        imageButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "BeerKeeper.fileprovider",
                    createCapturedPhoto()
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(takePictureIntent, CAMERA_REQUEST)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeGalleryButton() {
        galleryButton.setOnClickListener {
            val choosePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
            choosePictureIntent.type = "image/*"

            try {
                startActivityForResult(choosePictureIntent, REQUEST_PICK_IMAGE)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(context, "Unable to open gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeLocationButton() {
        locationButton.setOnClickListener {
            startActivityForResult(
                Intent(requireContext(), EditMapActivity::class.java),
                MAP_REQUEST
            )
        }
    }

    private fun initializeTasteButton() {
        tasteButton.setOnClickListener {
            val checked = type.findViewById<RadioButton>(type.checkedRadioButtonId)
            var type: String = "Ale"
            when (checked.id) {
                aleButton.id -> {
                    type = "Ale"
                }
                lagerButton.id -> {
                    type = "Lager"
                }
                hybridButton.id -> {
                    type = "Hybrid"
                }
            }
            viewModel.saveChanges(
                Beer(
                    beerId,
                    name.text.toString(),
                    abv.text.toString().toDouble(),
                    type,
                    AuthRepository.user!!._id
                )
            )
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "PHOTO_${timeStamp}.jpg"
        val file = File(storageDir, fname)
        currentPhotoPath = file.absolutePath
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun revealView(view: View) {
        view.apply {
            alpha = 0f
            view.visibility = View.VISIBLE
            animate().alpha(1f).setDuration(1000).setListener(null)
        }
    }
}