package com.example.rayaan.Daycare.Specialist.WokingHours

import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Session
import com.google.firebase.database.*
import com.weiwangcn.betterspinner.library.BetterSpinner

import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.example.rayaan.R

class WorkingHourActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var workinglist: ArrayList<Working>
    lateinit var adap: WorkingAdapter
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var day: BetterSpinner
    lateinit var from:EditText
    lateinit var to:EditText
    internal var mDatabase: DatabaseReference? = null
    var id = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_working_hour)
        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }
        Log.d("aboooood",id)

        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        mDatabase = FirebaseDatabase.getInstance().reference
        workinglist = ArrayList<Working>()
        adap = WorkingAdapter(workinglist,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        day = findViewById(R.id.day)
        from = findViewById(R.id.from)
        to = findViewById(R.id.to)
        getTime(from,this)
        getTime(to,this)

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.days, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        day.setAdapter(adapter)
        var add:Button = findViewById(R.id.add)
       add.visibility = View.GONE
        day.visibility = View.GONE
        from.visibility = View.GONE
        to.visibility = View.GONE

        getDays()
    }
    private fun AddDay() {
        val uniqueKey = mDatabase!!.child("Users").push().key
        mDatabase!!.child("Users").child(id).child("workinghour").child(uniqueKey.toString())
            .child("day").setValue(day.text.toString())

        mDatabase!!.child("Users").child(id).child("workinghour").child(uniqueKey.toString())
            .child("time").setValue(from.text.toString() +"-" + to.text.toString())

        day.setText("")
        from.setText("")
        to.setText("")
        getDays()

    }
    private fun getDays() {
        workinglist.clear()
        val query = mDatabase!!.child("Users").child(id).child("workinghour")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        workinglist.add(Working(
                                user.key.toString()
                                , user.child("time").getValue().toString(), user.child("day").getValue().toString()
                            )
                        )
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
    fun getTime(textt: EditText, context: Context){

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            textt.setText(SimpleDateFormat("HH:mm:ss").format(cal.time))
        }

        textt.setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }

}
data class Working(val id:String,val time:String, val day: String)
