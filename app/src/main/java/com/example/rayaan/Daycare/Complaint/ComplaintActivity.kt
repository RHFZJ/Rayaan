package com.example.rayaan.Daycare.Complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ComplaintActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var complist: ArrayList<comp>
    lateinit var adap: CompAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var desc: EditText
    internal var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint)

        mDatabase = FirebaseDatabase.getInstance().reference

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        complist = ArrayList<comp>()
        adap = CompAdapter(complist,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        getComp()
        desc = findViewById(R.id.desc)
        var send: Button = findViewById(R.id.send)
        send.setOnClickListener(View.OnClickListener {
                if (desc.text.toString().isEmpty()) {
                    desc.setError(" الرجاء وصف الشكوى")
                } else {
                    val uniqueKey = mDatabase!!.child("Complaint").push().key

                    mDatabase!!.child("Complaint").child(uniqueKey.toString())
                        .child("title").setValue(desc.text.toString())

                    mDatabase!!.child("Complaint").child(uniqueKey.toString())
                        .child("senderid").setValue(session.getId().toString())

                    var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    var currentDateandTime = sdf.format( Date());
                    mDatabase!!.child("Complaint").child(uniqueKey.toString())
                        .child("date").setValue(currentDateandTime.toString())

                    Toast.makeText(
                        this@ComplaintActivity,
                        "تم اضافة الشكوى",
                        Toast.LENGTH_SHORT
                    ).show()
                    getComp()
                }

        })
    }

    private fun getComp() {
        complist.clear()
        val query = mDatabase!!.child("Complaint")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(session.getId().toString().equals(user.child("senderid").getValue().toString())) {
                            complist.add(
                                comp(user.key.toString(),user.child("replay").getValue().toString()
                                    , user.child("title").getValue().toString(), user.child("date").getValue().toString())
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
data class comp(val id:String,val replay:String,val desc: String,val date:String)

