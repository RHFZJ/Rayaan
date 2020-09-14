package com.example.rayaan.Daycare.License

import android.content.Intent
import android.os.Bundle
import android.view.View

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

import java.util.*

class LicenseActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var lis: ArrayList<License>
    lateinit var adap: LicenseAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    internal var mDatabase: DatabaseReference? = null
    lateinit var add: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_license)
        mDatabase = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        lis = ArrayList<License>()
        adap = LicenseAdapter(lis,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        add =findViewById(R.id.add);
        add.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, AddLicenseActivity::class.java))
        })
    }

    override fun onResume() {
        super.onResume()
        getDAta()
    }
    private fun getDAta() {
        lis.clear()
        val query = mDatabase!!.child("Users").child(session.getId().toString()).child("license")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        lis.add(License(user.key.toString()
                                ,user.child("photo").getValue().toString(),user.child("type").getValue().toString()))

                    }
                    adap.notifyDataSetChanged()
                    if(lis.size == 0){
                        Toast.makeText(
                            this@LicenseActivity,
                            "لا يوجد شهادات",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@LicenseActivity,
                        "لا يوجد شهادات",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }
}
data class License(val id:String,val photo:String, val type: String)
