package `in`.testdemo.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


private const val LOCATION_PERMISSION_REQUEST_CODE = 1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {// EasyPermissions.PermissionCallbacks {

    private lateinit var mMap: GoogleMap

    private var showPermissionDeniedDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        getLocation()
    }


//    @AfterPermissionGranted(LOCATION_PERMISSION_REQUEST_CODE)
//    private fun enableMyLocation() {
//        // Enable the location layer. Request the location permission if needed.
//        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
//
//        if (EasyPermissions.hasPermissions(this, *permissions)) {
//            mMap.isMyLocationEnabled = true
//
//        } else {
//            // if permissions are not currently granted, request permissions
//            EasyPermissions.requestPermissions(
//                this,
//                getString(R.string.permission_rationale_location),
//                LOCATION_PERMISSION_REQUEST_CODE, *permissions
//            )
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(
//            requestCode,
//            permissions, grantResults, this
//        )
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
//        // Un-check the box until the layer has been enabled
//        // and show dialog box with permission rationale.
////        myLocationCheckbox.isChecked = false
//        showPermissionDeniedDialog = true
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        // do nothing, handled in updateMyLocation
//    }
//
//    /**
//     * Display a dialog box asking the user to grant permissions if they were denied
//     */
//    override fun onResumeFragments() {
//        super.onResumeFragments()
//        if (showPermissionDeniedDialog) {
//            AlertDialog.Builder(this).apply {
//                setPositiveButton(R.string.ok, null)
//                setMessage(R.string.location_permission_denied)
//                create()
//            }.show()
//            showPermissionDeniedDialog = false
//        }
//    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            when (grantResults[0]) {
                PackageManager.PERMISSION_GRANTED -> getLocation()
                PackageManager.PERMISSION_DENIED -> {

                }//Tell to user the need of grant permission
            }
        }
    }


    fun getLocation() {

        var locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

        var locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                var latitute = location!!.latitude
                var longitute = location!!.longitude

                Log.i("test", "Latitute: $latitute ; Longitute: $longitute")

                val location = LatLng(latitute, longitute)

                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                val addresses = geocoder.getFromLocation(latitute, longitute, 1)
                val cityName = addresses[0].getAddressLine(0)

                mMap.addMarker(MarkerOptions().position(location).title(cityName))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }

        }

//        try {
//            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
//        } catch (ex:SecurityException) {
//            Toast.makeText(applicationContext, "Fehler bei der Erfassung!", Toast.LENGTH_SHORT).show()
//        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
    }

}
