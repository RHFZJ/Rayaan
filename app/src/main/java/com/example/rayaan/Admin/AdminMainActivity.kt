package com.example.rayaan.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.rayaan.*
import com.example.rayaan.Admin.Daycare.Complaint.ComplaintActivity
import com.example.rayaan.Admin.Daycare.DayCareActivity
import com.example.rayaan.Admin.Parent.ParentActivity
import com.example.rayaan.Admin.Specialist.SpecialistActivity
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class AdminMainActivity : AppCompatActivity() {

    lateinit var profile: Button
    lateinit var password: Button
    lateinit var logout: Button
    lateinit var daycare :Button
    lateinit var specialist:Button
    lateinit var complaint:Button
    lateinit var parent:Button

    lateinit var logo: ImageView



    lateinit var session:Session
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        session = Session(this)

        profile = findViewById(R.id.profile)
        password = findViewById(R.id.password)
        logout = findViewById(R.id.logout)
        daycare = findViewById(R.id.daycare)
        complaint= findViewById(R.id.complaint)
        parent= findViewById(R.id.parent)

        logo = findViewById(R.id.logo)
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+session.getImage());
        Glide.with(baseContext)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(resources.getDrawable(R.drawable.logo))
            .into(logo)


        specialist= findViewById(R.id.specialist)


        profile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ProfileActivity::class.java))

        })
        complaint.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ComplaintActivity::class.java))

        })

        password.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, PasswordActivity::class.java))
        })

        daycare.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, DayCareActivity::class.java))
        })

        parent.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ParentActivity::class.java))
        })

        specialist.setOnClickListener(View.OnClickListener {
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
    }
}
