package com.example.rayaan.Parent.Specialist;

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rayaan.Admin.Daycare.License.License
import com.example.rayaan.Admin.Daycare.License.LicenseAdapter
import com.example.rayaan.Admin.Daycare.Working.Working
import com.example.rayaan.Admin.Daycare.Working.WorkingAdapter
import com.example.rayaan.Daycare.Specialist.Ads.AdsActivity
import com.example.rayaan.Daycare.Specialist.ChatActivity
import com.example.rayaan.Parent.DayCare.Worker.WorkerActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.ArrayList

class SpecialistDetailsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var recyclerView2: RecyclerView

    lateinit var session: Session
    lateinit var working: ArrayList<Working>
    lateinit var license: ArrayList<License>

    lateinit var adap: WorkingAdapter
    lateinit var adap2: LicenseAdapter

    internal var mDatabase: DatabaseReference? = null
    internal var mDatabase2: DatabaseReference? = null

    var id:String = ""
    private var gridLayoutManager2: GridLayoutManager? = null

    lateinit var name:TextView
    lateinit var mobile:TextView
    lateinit var email:TextView
    lateinit var price:TextView
    lateinit var price2:TextView


    lateinit var chat:Button
    lateinit var location:Button
    lateinit var ads:Button

    lateinit var image:ImageView

    lateinit var worker:Button


    var longi = ""
    var latt = ""
    var username:String = ""
    var type:String = ""


    private var gridLayoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_details)

        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
            username = b.getString("username").toString()

        }
        recyclerView2 = findViewById(R.id.recycler_view2) as RecyclerView

        name = findViewById(R.id.name)
        mobile = findViewById(R.id.mobile)
        email = findViewById(R.id.email)
        chat = findViewById(R.id.chat)
        ads = findViewById(R.id.ads)

        price = findViewById(R.id.price)
        price2 = findViewById(R.id.price2)
        worker = findViewById(R.id.worker)

        image = findViewById(R.id.image)
        location = findViewById(R.id.location)
        location.visibility = View.GONE
        price.visibility = View.GONE
        price2.visibility = View.GONE

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase2 = FirebaseDatabase.getInstance().reference

        working = ArrayList<Working>()
        license = ArrayList<License>()


        chat.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext,
                ChatActivity::class.java).putExtra("sname",username + "-" +session.getUsername()))
        })

        ads.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext,
                AdsActivity::class.java).putExtra("id",id))
        })


        worker.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext,
                WorkerActivity::class.java).putExtra("id",id))
        })

        adap = WorkingAdapter(working,this,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap

        adap2 = LicenseAdapter(license,this,this)
        gridLayoutManager2 = GridLayoutManager(this, 1)
        recyclerView2.layoutManager = gridLayoutManager2
        recyclerView2.adapter = adap2
        getDaycare()
        getDAta()
        getContact()
    }
    private fun getContact() {
        val query = mDatabase2!!.child("Users").child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                        name.setText(dataSnapshot.child("name").getValue().toString())
                        mobile.setText(dataSnapshot.child("mobile").getValue().toString())
                        email.setText(dataSnapshot.child("email").getValue().toString())

                type = dataSnapshot.child("type").getValue().toString()
                if(!type.equals("daycare")){
                    worker.visibility = View.GONE
                }else{
                    worker.visibility = View.VISIBLE
                }
                        var storage = FirebaseStorage.getInstance();
                        var storageReference = storage!!.getReference()?.child("images/"+dataSnapshot.child("photo").getValue().toString());
                        Glide.with(baseContext)
                            .using(FirebaseImageLoader())
                            .load(storageReference)
                            .placeholder(resources.getDrawable(R.drawable.img))
                            .into(image)
                        if(dataSnapshot.child("Latitude").exists()){
                            longi = dataSnapshot.child("Longitude").getValue().toString()
                            latt = dataSnapshot.child("Latitude").getValue().toString()
                            location.visibility = View.VISIBLE
                            location.setOnClickListener(View.OnClickListener {
                                val gmmIntentUri = Uri.parse("google.navigation:q=" + latt + "," + longi)
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                mapIntent.`package` = "com.google.android.apps.maps"
                                startActivity(mapIntent)
                            })
                        }
                        if(dataSnapshot.child("Price").exists()){
                            price.visibility = View.VISIBLE
                            price2.visibility = View.VISIBLE
                            price.setText(dataSnapshot.child("Price").getValue().toString() +    " ريال ")

                        }



            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    private fun getDAta() {
        val query = mDatabase!!.child("Users").child(id).child("license")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {

                        license.add(License(user.key.toString()
                            ,user.child("photo").getValue().toString(),user.child("type").getValue().toString()))

                    }
                    adap2.notifyDataSetChanged()
                    if(license.size == 0){
                        Toast.makeText(
                            this@SpecialistDetailsActivity,
                            "لا يوجد شهادات",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@SpecialistDetailsActivity,
                        "لا يوجد شهادات",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getDaycare() {
        val query = mDatabase!!.child("Users").child(id).child("workinghour")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                            working.add(
                                Working(user.child("day").getValue().toString(),user.child("time").getValue().toString()
                                ,user.child("day").getValue().toString())
                            )
                        }

                    adap.notifyDataSetChanged()
                    if(working.size == 0){
                        Toast.makeText(
                            this@SpecialistDetailsActivity,
                            "لا يوجد ساعات عمل",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@SpecialistDetailsActivity,
                        "لا يوجد ساعات عمل",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
