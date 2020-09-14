package com.example.rayaan.Daycare

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.database.*

class LocationPriceActivity : AppCompatActivity() {

    private val PLACE_PICKER_REQUEST = 1
    private var mGoogleApiClient: GoogleApiClient? = null

    internal var longi = " "
    internal var latt = " "

    internal var mDatabase: DatabaseReference? = null
    lateinit  var session: Session

    lateinit var price:EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_price)

        buildGoogleApiClient()
        mDatabase = FirebaseDatabase.getInstance().reference
        session = Session(this)
        price = findViewById(R.id.price);


        val query = mDatabase!!.child("Users").child(session.getId().toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("Latitude").exists()) {
                    latt =dataSnapshot.child("Latitude").getValue().toString()

                }
                if (dataSnapshot.child("Longitude").exists()) {
                    longi =dataSnapshot.child("Longitude").getValue().toString()

                }
                if (dataSnapshot.child("Price").exists()) {
                    price.setText(dataSnapshot.child("Price").getValue().toString())
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })



        var location: Button = findViewById(R.id.location)
        location.setOnClickListener({

            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
        })

        var add: Button = findViewById(R.id.add)
        add.setOnClickListener({

            Toast.makeText(baseContext,"تم تعديل البيانات",Toast.LENGTH_SHORT).show()
            mDatabase!!.child("Users").child(session.getId().toString()).child("Latitude").setValue(latt)
            mDatabase!!.child("Users").child(session.getId().toString()).child("Longitude").setValue(longi)
            mDatabase!!.child("Users").child(session.getId().toString()).child("Price").setValue(price.text.toString())
        })



    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

           if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
                val place = PlacePicker.getPlace(data, this)
                val latitude = place.latLng.latitude.toString()
                val longitude = place.latLng.longitude.toString()
                val address = String.format("%s", place.address)
                longi = longitude
                latt = latitude
                val toastMsg = String.format("Place: %s", place.name)
            }

        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .enableAutoManage(this, null)
            .build()
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Locations Settings is set to 'Off'.\nEnable Location to use this app")
            .setPositiveButton("Location Settings") { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> }
        dialog.show()
    }
}
