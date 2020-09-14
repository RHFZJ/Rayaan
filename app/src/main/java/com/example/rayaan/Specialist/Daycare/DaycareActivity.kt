package com.example.rayaan.Specialist.Daycare;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import java.util.ArrayList

class DaycareActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var userslist: ArrayList<DayCare>
    lateinit var adap: DaycareAdapter
    internal var mDatabase: DatabaseReference? = null

    private var gridLayoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_care)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        userslist = ArrayList<DayCare>()
        adap = DaycareAdapter(userslist,this,this)
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
                        if(user.child("type").value!!.toString() == "daycare"){
                            if( user.child("Price").exists()) {

                                userslist.add(
                                    DayCare(
                                        user.key.toString()
                                        ,
                                        user.child("name").getValue().toString(),
                                        user.child("username").getValue().toString()
                                        ,
                                        user.child("mobile").getValue().toString(),
                                        user.child("email").getValue().toString()
                                        ,
                                        user.child("password").getValue().toString(),
                                        user.child("Price").getValue().toString()
                                    )
                                )
                            }else{
                                userslist.add(
                                    DayCare(
                                        user.key.toString()
                                        ,
                                        user.child("name").getValue().toString(),
                                        user.child("username").getValue().toString()
                                        ,
                                        user.child("mobile").getValue().toString(),
                                        user.child("email").getValue().toString()
                                        ,
                                        user.child("password").getValue().toString(),
                                       "no"
                                    )
                                )
                            }

                        }
                    }
                    adap.notifyDataSetChanged()
                    if(userslist.size == 0){
                        Toast.makeText(
                            this@DaycareActivity,
                            "لا يوجد حضانات",
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
                 val mail: String, val pass: String,val price: String)
