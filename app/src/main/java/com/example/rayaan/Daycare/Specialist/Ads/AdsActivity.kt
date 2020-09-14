package com.example.rayaan.Daycare.Specialist.Ads

import android.content.Intent
import android.os.Bundle

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Session
import com.google.firebase.database.*

import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.example.rayaan.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdsActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var list: ArrayList<Ads>
    lateinit var adap: AdsAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    internal var mDatabase: DatabaseReference? = null

    lateinit var add:FloatingActionButton

    var id = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ads)

        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }

        add =findViewById(R.id.add);
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mDatabase = FirebaseDatabase.getInstance().reference
        list = ArrayList<Ads>()
        adap = AdsAdapter(list,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        add.visibility = View.GONE


    }

    override fun onResume() {
        super.onResume()
        getAds()

    }
    private fun getAds() {
        list.clear()
        val query = mDatabase!!.child("Ads")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(id.equals(user.child("userid").getValue().toString())) {
                            list.add(
                                Ads(
                                    user.key.toString()
                                    ,
                                    user.child("content").getValue().toString(),
                                    user.child("date").getValue().toString()
                                    ,
                                    user.child("photo").getValue().toString(),
                                    user.child("title").getValue().toString()
                                )
                            )
                        }
                    }
                    adap.notifyDataSetChanged()
                    if(list.size == 0){
                        Toast.makeText(
                            this@AdsActivity,
                            "لا يوجد اعلانات",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@AdsActivity,
                        "لا يوجد اعلانات",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
data class Ads(val id:String,val content:String, val date: String, val photo: String, val title: String)
