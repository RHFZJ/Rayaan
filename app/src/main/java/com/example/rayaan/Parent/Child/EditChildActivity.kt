package com.example.rayaan.Parent.Child

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import com.weiwangcn.betterspinner.library.BetterSpinner
import java.text.SimpleDateFormat
import java.util.*

internal class EditChildActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var dob: EditText
    lateinit var status: BetterSpinner
    lateinit var add: Button

    lateinit var session: Session
    internal var mDatabase: DatabaseReference? = null
    var id:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_child)

        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }

        name = findViewById(R.id.name);
        dob = findViewById(R.id.dob);
        status = findViewById(R.id.status);
        add = findViewById(R.id.add);

        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference


        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dob.setText(sdf.format(cal.time))

        }

        dob.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }


        val adapter = ArrayAdapter.createFromResource(this,
            R.array.status, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        status.setAdapter(adapter)

        add.setOnClickListener(View.OnClickListener {
            if (name.text.toString().isEmpty()) {
                name.setError("الرجاء ادخال الاسم")
            } else if (dob.text.toString().isEmpty()) {
                dob.setError("الرجاء ادخال تاريخ الميلاد")
            }else if (status.text.toString().isEmpty()) {
                status.setError("الرجاء تحديد حالة الطفل")
            } else {

                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(id)
                    .child("name").setValue(name.text.toString())
                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(id)
                    .child("dob").setValue(dob.text.toString())
                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(id)
                    .child("status").setValue(status.text.toString())
                Toast.makeText(this@EditChildActivity, "تم تعديل الطفل", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
        getDAta()
    }
    private fun getDAta() {
        val query = mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    name.setText(dataSnapshot.child("name").getValue().toString())
                    status.setText(dataSnapshot.child("status").getValue().toString())
                    dob.setText(dataSnapshot.child("dob").getValue().toString())


                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}