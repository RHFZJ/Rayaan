package com.example.rayaan.Daycare.Specialist.Child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.ArrayList

class ChildActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var userslist: ArrayList<Child>
    lateinit var adap: ChildAdapter
    internal var mDatabase: DatabaseReference? = null
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var add: FloatingActionButton

    var username = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)

        val i = intent
        val b = i.extras
        if (b != null) {
            username = b.getString("username").toString()
        }

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mDatabase = FirebaseDatabase.getInstance().reference
        userslist = ArrayList<Child>()
        adap = ChildAdapter(userslist,this,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        add =findViewById(R.id.add);
        add.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()
        getChild()

    }
    private fun getChild() {
        userslist.clear()
        val query = mDatabase!!.child("Users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {

                        if(user.child("username").getValue().toString().equals(username)) {
                            for (user2 in user.child("Child").children) {
                                userslist.add(
                                    Child(
                                        user2.key.toString()
                                        , user2.child("name").getValue().toString()
                                        ,user2.child("dob").getValue().toString()
                                        ,user2.child("status").getValue().toString()
                                    )
                                )

                            }
                        }

                    }
                    adap.notifyDataSetChanged()
                    if(userslist.size == 0){
                        Toast.makeText(
                            this@ChildActivity,
                            "لا يوجد اطفال",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                        Toast.makeText(
                            this@ChildActivity,
                            "لا يوجد اطفال",
                            Toast.LENGTH_SHORT
                        ).show()

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
data class Child(val id:String,val name:String , val birthdate: String, val status: String)

