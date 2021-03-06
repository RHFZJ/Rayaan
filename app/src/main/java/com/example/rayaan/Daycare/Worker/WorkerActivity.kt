package com.example.rayaan.Daycare.Worker;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Ads.AddAdsActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.ArrayList

class WorkerActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var userslist: ArrayList<DayCare>
    lateinit var userslist2: ArrayList<DayCare>

    lateinit var adap: WorkerAdapter
    internal var mDatabase: DatabaseReference? = null
    lateinit var add: FloatingActionButton

    private var gridLayoutManager: GridLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day_care2)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        userslist = ArrayList<DayCare>()
        userslist2 = ArrayList<DayCare>()
        add =findViewById(R.id.add);

        adap = WorkerAdapter(userslist2,this,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap

        add.visibility = View.GONE
//        add.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(baseContext, AddWorkerActivity::class.java))
//        })

    }

    override fun onResume() {
        super.onResume()
        getSpec()
    }


    private fun getWorker() {
        val query = mDatabase!!.child("Users").child(session.getId().toString()).child("worker")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(user.child("id").exists()) {
                            for (a in userslist) {

                                if (a.id.equals(user.child("id").getValue().toString())) {
                                    a.id = user.key.toString()
                                    userslist2.add(a)
                                }
                            }
                        }
                        else{
//                        userslist2.add(
//                            DayCare(
//                                user.key.toString()
//                                ,
//                                user.child("name").getValue().toString(),
//                                user.child("specialist").getValue().toString(),
//                                user.child("type").getValue().toString(),
//                                "",
//                                "",
//                                "",user.key.toString())
//                        )


                    }


                    }
                    adap.notifyDataSetChanged()
                    if(userslist2.size == 0){
                        Toast.makeText(
                            this@WorkerActivity,
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

    private fun getSpec() {
        userslist.clear()
        userslist2.clear()

        val query = mDatabase!!.child("Users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(user.child("type").value!!.toString() == "specialist"){
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
                                        user.child("Price").getValue().toString(), user.key.toString())
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
                                       "no", user.key.toString()))
                            }
                        }
                    }
                    getWorker()
//                    adap.notifyDataSetChanged()
//                    if(userslist.size == 0){
//                        Toast.makeText(
//                            this@SpecialistActivity,
//                            "لا يوجد مختصين",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
data class DayCare(
    var id:String, val name:String, val username: String, val mobile: String,
    val mail: String, val pass: String, val price:String,var sid:String)
