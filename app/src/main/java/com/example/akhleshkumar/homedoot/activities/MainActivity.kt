package com.example.akhleshkumar.homedoot.activities

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.akhleshkumar.homedoot.HomeDootFragment
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.fragments.CartFragment
import com.example.akhleshkumar.homedoot.fragments.ProfileFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor
    lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("HomeDoot", MODE_PRIVATE)
        editorSP = sharedPreferences.edit()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            getLastLocation()
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        id = intent.getIntExtra("id",0)

        val homeDootFragment = HomeDootFragment()
        val args = Bundle()
        args.putInt("id", id)
        homeDootFragment.arguments = args
        val cartFragment = CartFragment()
        cartFragment.arguments = args
        val profileFragment =  ProfileFragment()
        profileFragment.arguments = args
        loadFragment(homeDootFragment)

        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            var fragment: Fragment? = null
            val itemId = item.itemId

            if (itemId == R.id.navigation_hd) {
                fragment = homeDootFragment
            } else if (itemId == R.id.navigation_home) {
                fragment = homeDootFragment
            } else if (itemId == R.id.navigation_Account) {
                fragment = profileFragment
            }else if(itemId == R.id.navigation_cart){
                fragment = cartFragment
            }
            loadFragment(fragment)
        })

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container, fragment)
            ft.commit()



            return true
        }
        return false
    }


private fun getLastLocation() {
    if (ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val locationTask: Task<Location> = fusedLocationClient.lastLocation
        locationTask.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddress(location.latitude, location.longitude)
            } else {
                Log.e("Location Error", "Unable to retrieve location")
            }
        }
    }else{
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

    }
}

private fun getAddress(latitude: Double, longitude: Double) {
    val geocoder = Geocoder(this, Locale.getDefault())
    val addresses: List<Address>?
    try {
        addresses = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            val fullAddress = address.getAddressLine(0) // Full address
            val postalCode = address.postalCode // Pin code
            val city = address.locality // City name
            val state = address.adminArea // State name
            val homeNumber = address.subThoroughfare // Home number
            val villageOrSector = address.thoroughfare // Village or sector name

            editorSP.putString("fullAddress", fullAddress)
            editorSP.putString("pinCode", postalCode)
            editorSP.putString("city", city)
            editorSP.putString("state", state)
            editorSP.putString("homeNumber", homeNumber)
            editorSP.putString("villageOrSector", villageOrSector)
            editorSP.commit()
            Log.d("Address", "Address: $fullAddress, Postal Code: $postalCode")
        } else {
            Log.e("Geocoder Error", "No address found")
        }
    } catch (e: Exception) {
        Log.e("Geocoder Error", e.message.toString())
    }
}

override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getLastLocation()
        } else {
            Log.e("Permission Error", "Location permission denied")
        }
    }
}

companion object {
    private const val LOCATION_PERMISSION_REQUEST_CODE = 1
}
}