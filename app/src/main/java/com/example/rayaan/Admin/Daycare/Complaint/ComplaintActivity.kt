package com.example.rayaan.Admin.Daycare.Complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.google.firebase.database.*
import java.util.*

class ComplaintActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var complist: ArrayList<comp>
    lateinit var adap: CompAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    internal var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_complaint)
        mDatabase = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        complist = ArrayList<comp>()
        adap = CompAdapter(complist,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        getComp()
    }
    private fun getComp() {
        var users:DataSnapshot? = null
        complist.clear()
        val query = mDatabase!!.child("Complaint")
        val query2 = mDatabase!!.child("Users")
        query2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                   users =  dataSnapshot

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(!users!!.child(user.child("senderid").getValue().toString()).child("username").getValue().toString().equals("null")){

                            complist.add(
                                comp(user.key.toString()," "
                                    , user.child("title").getValue().toString(), user.child("date").getValue().toString(),
                                    users!!.child(user.child("senderid").getValue().toString()).child("username").getValue().toString()
                                   )
                                )
                        }

                    }
                    adap.notifyDataSetChanged()
                    if(complist.size == 0){
                        Toast.makeText(
                            this@ComplaintActivity,
                            "لا يوجد شكاوى",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@ComplaintActivity,
                        "لا يوجد شكاوى",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
}
data class comp(val id:String, var replay:String, val desc: String, val date:String, var username:String)

