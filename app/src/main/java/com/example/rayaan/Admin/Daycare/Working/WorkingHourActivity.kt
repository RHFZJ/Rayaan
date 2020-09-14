package com.example.rayaan.Admin.Daycare.Working

import android.os.Bundle

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*

import java.util.*

class WorkingHourActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var workinglist: ArrayList<Working>
    lateinit var adap: WorkingAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    var id:String = ""
    internal var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_working_hour)


        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }
        mDatabase = FirebaseDatabase.getInstance().reference

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)

        workinglist = ArrayList<Working>()
        adap = WorkingAdapter(workinglist,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        getDays()
    }
    private fun getDays() {
        val query = mDatabase!!.child("Users").child(id).child("workinghour")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {

                        workinglist.add(Working(user.key.toString()
                                ,user.child("time").getValue().toString(),user.child("day").getValue().toString()))

                    }
                    adap.notifyDataSetChanged()
                    if(workinglist.size == 0){
                        Toast.makeText(
                            this@WorkingHourActivity,
                            "لا يوجد ساعات عمل",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@WorkingHourActivity,
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
data class Working(val id:String,val time:String, val day: String)
