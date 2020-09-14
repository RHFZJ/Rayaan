package com.example.rayaan.Parent.Specialist;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import java.util.ArrayList

class SpecialistActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var userslist: ArrayList<DayCare>
    lateinit var adap: SpecialistAdapter
    internal var mDatabase: DatabaseReference? = null

    private var gridLayoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_care)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        userslist = ArrayList<DayCare>()
        adap = SpecialistAdapter(userslist,this,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        getDaycare()
    }
    private fun getDaycare() {
        val query = mDatabase!!.child("Users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(user.child("type").value!!.toString() == "specialist"){
                            userslist.add(
                                DayCare(user.key.toString()
                                ,user.child("name").getValue().toString(),user.child("username").getValue().toString()
                                ,user.child("mobile").getValue().toString(),user.child("email").getValue().toString()
                                ,user.child("password").getValue().toString(),user.child("photo").getValue().toString())
                            )
                        }
                    }
                    adap.notifyDataSetChanged()
                    if(userslist.size == 0){
                        Toast.makeText(
                            this@SpecialistActivity,
                            "لا يوجد مختصين",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
data class DayCare(val id:String,val name:String , val username: String, val mobile: String,
                 val mail: String, val pass: String,val photo:String)
