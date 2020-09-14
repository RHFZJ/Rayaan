package com.example.rayaan.Daycare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.rayaan.*
import com.example.rayaan.Daycare.Ads.AdsActivity
import com.example.rayaan.Daycare.Complaint.ComplaintActivity
import com.example.rayaan.Daycare.License.LicenseActivity
import com.example.rayaan.Daycare.Worker.WorkerActivity
import com.example.rayaan.Daycare.WorkingHours.WorkingHourActivity
import com.example.rayaan.Parent.ChatActivity
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class DaycareMainActivity : AppCompatActivity() {

    lateinit var profile: Button
    lateinit var password: Button
    lateinit var logout: Button
    lateinit var location : Button
    lateinit var license:Button
    lateinit var working: Button
    lateinit var ads:Button
    lateinit var complaint:Button
    lateinit var spec:Button
    lateinit var chat:Button
    lateinit var worker:Button
    lateinit var logo: ImageView

    lateinit var session: Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daycare_main)


        session = Session(this)
        profile = findViewById(R.id.profile)
        password = findViewById(R.id.password)
        worker = findViewById(R.id.worker)
        logo = findViewById(R.id.logo)

        chat = findViewById(R.id.chat)

        chat.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ChatActivity::class.java))

        })


        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+session.getImage());
        Glide.with(baseContext)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(resources.getDrawable(R.drawable.logo))
            .into(logo)

        spec = findViewById(R.id.spec)
        logout = findViewById(R.id.logout)
        location = findViewById(R.id.location)
        ads = findViewById(R.id.ads);
        complaint = findViewById(R.id.complaint)
        license = findViewById(R.id.license);

        working= findViewById(R.id.working)


        profile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))

        })
        worker.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, WorkerActivity::class.java))

        })

        password.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, PasswordActivity::class.java))
        })
        spec.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, com.example.rayaan.Daycare.Specialist.SpecialistActivity::class.java))
        })

        location.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, LocationPriceActivity::class.java))
        })

        working.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, WorkingHourActivity::class.java))
        })
        ads.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, AdsActivity::class.java))
        })

        complaint.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ComplaintActivity::class.java))
        })

        license.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, LicenseActivity::class.java))
        })

        logout.setOnClickListener(View.OnClickListener {
            session.setLogin(false);
            session.setType("");
            session.setId("12");
            val i = Intent(baseContext, MainActivity::class.java)
            startActivity(i);
            finish();

        })
    }
}
