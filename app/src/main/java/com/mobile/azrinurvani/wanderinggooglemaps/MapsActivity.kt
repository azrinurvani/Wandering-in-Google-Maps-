package com.mobile.azrinurvani.wanderinggooglemaps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

//TODO: Step 1.0 Create new Project using MapsActivity template
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //TODO: Step 1.2 Refactor mMap to map
    private lateinit var map: GoogleMap

    //TODO: Step 5.1 Navigate to https://mapstyle.withgoogle.com/ in your browser.
    // Select Create a Style
    // Select the Retro theme.
    // Click More Options at the bottom of the menu.
    // In the Feature type list, select Road > Fill. Change the color of the roads to any color you choose (such as pink).
    // Click Finish. Copy the JSON code from the resulting pop-up window.
    // Create a file in res/raw called map_style.json.
    // Paste the JSON code into the new resource file.

    //TODO: Step 5.2 Create a TAG class variable above the onCreate() method. This will be used for logging purposes.
    private val TAG = MapsActivity::class.java.simpleName

    //TODO: Step 8.1 Create a REQUEST_LOCATION_PERMISSION variable.
    private val REQUEST_LOCATION_PERMISSION = 1

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
        map = googleMap

       //TODO: Step 2.1 Create a value for the latitude and a value for the longitude and LatLng object
        val latitude = -0.920096
        val longitude =  100.463842
        val homeLatLng = LatLng(latitude,longitude)

        //TODO: Step 7.2 Create a float for the width in meters of the desired overlay.
        // For this example, a width of 100f works well. Pass in the home LatLng object and the size value
        val overLaySize = 100f

        //TODO: Step 2.2 Create a val for how zoomed in you want to be on the map.
        //1: World
        //5: Landmass/continent
        //10: City
        //15: Streets
        //20: Buildings
        val zoomLevel = 18f //TODO: Step 7.5 Try changing the value of zoomLevel to 18f to see the Android image as an overlay.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng,zoomLevel))

        //TODO: Step 2.3 Add a marker to the map at your home.
        map.addMarker(MarkerOptions().position(homeLatLng))

        //TODO: Step 7.1  after the call to move the camera to your home’s position, create a GroundOverlayOptions object.
        // Assign the object to a variable called homeOverlay:
        val androidOverLay = GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
            //TODO: Step 7.3 Set the position property for the GroundOverlayOptions object by calling the position() method.
                .position(homeLatLng,overLaySize)

        //TODO: Step 7.4 Call addGroundOverlay() on the GoogleMap object. Pass in your GroundOverlayOptions object
        map.addGroundOverlay(androidOverLay)

        //TODO: Step 3.3 call setMapLongClick(). Pass in map.
        setMapLongClick(map)

        //TODO: Step 4.4 Call setPoiClick() at the end of onMapReady(). Pass in map.
        setPoiClick(map)

        //TODO: Step 5.5 Call the setMapStyle() method in the onMapReady() method passing in your GoogleMap object.
        setMapStyle(map)

        //TODO: Step 8.4 Call enableMyLocation() from the onMapReady() callback to enable the location layer.
        enableLocation()
    }

    //TODO: Step 1.5 Override the onCreateOptionsMenu() method and inflate the menu from the map_options resource file
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.map_options,menu)
        return true
    }

    //TODO: Step 1.6 override the onOptionsItemSelected() method
    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId){
        // Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    //TODO: Step 3.1 Create a method stub in MapsActivity called setMapLongClick() that takes a GoogleMap as an argument.
    // Attach a long click listener to the map object.
    private fun setMapLongClick(map: GoogleMap){

        map.setOnMapLongClickListener {latlng->

            //TODO: Step 3.4 Add an info window for the marker
            // A Snippet is Additional text that's displayed below the title.
            val snippet = String.format(
                    Locale.getDefault(),
                    "Lat: %1$.5f, Long: %2$.5f",
                    latlng.latitude,
                    latlng.longitude
            )

            //TODO: Step 3.2 Call the addMarker() method.
            // Pass in a new MarkerOptions object with the position set to the passed-in LatLng
            map.addMarker(MarkerOptions()
                            .position(latlng)
                        //TODO: Step 3.5 Set the title of the marker to “Dropped Pin”
                        // and set the marker’s snippet to the snippet you just created.
                            .title(getString(R.string.dropped_pin))
                            .snippet(snippet)
                        //TODO: Step 6.0 dd the following line of code to the MarkerOptions() of the constructor to use the default marker but change the color to blue
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
        }
    }

    //TODO: Step 4.1 Create a method stub in MapsActivity called setPoiClick() that takes a GoogleMap as an argument.
    private fun setPoiClick(map:GoogleMap){
        map.setOnPoiClickListener {poi->
            //TODO: Step 4.2 Place a marker at the POI location.
            // Set the title to the name of the POI.
            // Save the result to a variable called poiMarker.
            val poiMarker = map.addMarker(MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )
            //TODO: Step 4.3 In setOnPoiClickListener function, call showInfoWindow() on poiMarker to immediately show the info window.
            poiMarker.showInfoWindow()
        }
    }

    //TODO: Step 5.3 Create a setMapStyle() function that takes in a GoogleMap.
    private fun setMapStyle(map:GoogleMap){
        try{
            //TODO: Step 5.4  Pass in a MapStyleOptions object, which loads the JSON file.
            // The setMapStyle() method returns a boolean indicating the success of the styling.
            val success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.map_style
                    )
            )
            //If the styling is unsuccessful, print a log that the parsing has failed.
            if (!success){
                Log.e(TAG, "Style parsing failed.")
            }
        }catch (e: Resources.NotFoundException){
            Log.e(TAG, "Can't find style. Error: ", e)
        }

    }
    //TODO: Step 8.2 o check if permissions are granted, create a method in the MapsActivity.kt called isPermissionGranted().
    // In this method, check if the user has granted the permission.
    private fun isPermissionGranted() : Boolean{
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED
    }

    //TODO: Step 8.3 o enable location tracking in your app, create a method in the MapsActivity.kt called enableMyLocation()
    // that takes no arguments and doesn't return anything.
    // Check for the ACCESS_FINE_LOCATION permission. If the permission is granted, enable the location layer.
    // Otherwise, request the permission
    private fun enableLocation(){
        if (isPermissionGranted()){
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map?.isMyLocationEnabled = true
        }else{
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION)
        }
    }

    //TODO: Step 8.5 Override the onRequestPermissionsResult() method.
    // If the requestCode is equal to REQUEST_LOCATION_PERMISSION permission is granted,
    // and if the grantResults array is non empty with PackageManager.PERMISSION_GRANTED in its first slot, call enableMyLocation()
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode== REQUEST_LOCATION_PERMISSION){
            if (grantResults.isNotEmpty() &&(grantResults[0]==PackageManager.PERMISSION_GRANTED)){
                enableLocation()
            }
        }
    }
}
