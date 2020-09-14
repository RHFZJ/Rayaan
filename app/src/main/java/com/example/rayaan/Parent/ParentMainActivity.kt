package com.example.rayaan.Parent

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.rayaan.*
import com.example.rayaan.Daycare.Complaint.ComplaintActivity
import com.example.rayaan.Parent.Child.ChildActivity
import com.example.rayaan.Parent.DayCare.DayCareActivity
import com.example.rayaan.Parent.Specialist.SpecialistActivity
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ParentMainActivity : AppCompatActivity() {
    lateinit var profile: Button
    lateinit var password: Button
    lateinit var logout: Button
    lateinit var child : Button
    lateinit var spec: Button
    lateinit var session: Session
    lateinit var daycare: Button
    lateinit var chat: Button
    lateinit var complaint:Button
    lateinit var location: Button
    lateinit var logo: ImageView

    private val PLACE_PICKER_REQUEST = 1
    private var mGoogleApiClient: GoogleApiClient? = null

    internal var longi = " "
    internal var latt = " "

    internal var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_main)
        session = Session(this)
        buildGoogleApiClient()

        logo = findViewById(R.id.logo)
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+session.getImage());
        Glide.with(baseContext)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(resources.getDrawable(R.drawable.logo))
            .into(logo)

        mDatabase = FirebaseDatabase.getInstance().reference
        profile = findViewById(R.id.profile)
        password = findViewById(R.id.password)
        daycare = findViewById(R.id.daycare)
        logout = findViewById(R.id.logout)
        child = findViewById(R.id.child)
        chat = findViewById(R.id.chat)
        location = findViewById(R.id.location);
        complaint = findViewById(R.id.complaint)

        spec= findViewById(R.id.spec)

        profile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))
        })
        chat.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ChatActivity::class.java))

        })

        daycare.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, DayCareActivity::class.java))

        })

        password.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, PasswordActivity::class.java))
        })

        child.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ChildActivity::class.java))
        })

        complaint.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ComplaintActivity::class.java))
        })

        spec.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, SpecialistActivity::class.java))
        })
        logout.setOnClickListener(View.OnClickListener {
            session.setLogin(false);
            session.setType("");
            session.setId("12");
            val i = Intent(baseContext, MainActivity::class.java)
            startActivity(i);
            finish();

        })
        location.setOnClickListener({

            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
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

                Toast.makeText(baseContext,"تم تعديل الموقع", Toast.LENGTH_SHORT).show()
                mDatabase!!.child("Users").child(session.getId().toString()).child("Latitude").setValue(latt)
                mDatabase!!.child("Users").child(session.getId().toString()).child("Longitude").setValue(longi)
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
