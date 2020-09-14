package com.example.rayaan.Daycare.Complaint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ComplaintReplayActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var complist: ArrayList<complReplay>
    lateinit var adap: CompReplayAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var desc: EditText
    internal var mDatabase: DatabaseReference? = null
    var id= ""
    var title =""
    lateinit var titl:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complaint_replay)

        titl = findViewById(R.id.title)


        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
            title = b.getString("title").toString()
            titl.setText(title)

        }
        Log.d("aboooooooood",id)

        mDatabase = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        complist = ArrayList<complReplay>()
        adap = CompReplayAdapter(complist,baseContext,this)
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
                    if(session.getType().equals("admin")){
                        val uniqueKey = mDatabase!!.child("Complaint").push().key


                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("text").setValue(desc.text.toString())
                        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        var currentDateandTime = sdf.format( Date());
                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("date").setValue(currentDateandTime.toString())
                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("type").setValue("admin")

                    }else{
                        val uniqueKey = mDatabase!!.child("Complaint").push().key

                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("text").setValue(desc.text.toString())

                        var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        var currentDateandTime = sdf.format( Date());

                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("date").setValue(currentDateandTime.toString())

                        mDatabase!!.child("Complaint").child(id)
                            .child("replay").child(uniqueKey.toString()).child("type").setValue("user")
                    }

                    Toast.makeText(
                        this@ComplaintReplayActivity,
                        "تم اضافة الرد",
                        Toast.LENGTH_SHORT
                    ).show()


                    desc.setText("")
                    getComp()
                }

        })
    }

    private fun getComp() {
        complist.clear()
        val query = mDatabase!!.child("Complaint").child(id).child("replay")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        Log.d("abooooooooood",user.child("text").getValue().toString())
                            complist.add(
                                complReplay(user.child("text").getValue().toString()
                                    , user.child("date").getValue().toString(), user.child("type").getValue().toString())
                            )

                    }
                    adap.notifyDataSetChanged()
                    if(complist.size == 0){
                        Toast.makeText(
                            this@ComplaintReplayActivity,
                            "لا يوجد شكاوى",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@ComplaintReplayActivity,
                        "لا يوجد ردود",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

    }
}
data class complReplay(val text:String,val date: String,val flag:String)

