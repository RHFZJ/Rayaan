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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.weiwangcn.betterspinner.library.BetterSpinner
import java.text.SimpleDateFormat
import java.util.*

internal class AddChildActivity : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var dob: EditText
    lateinit var status: BetterSpinner
    lateinit var add: Button

    lateinit var session: Session
    internal var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_child)
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

                val uniqueKey = mDatabase!!.child("Users").push().key
                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(uniqueKey.toString())
                    .child("name").setValue(name.text.toString())

                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(uniqueKey.toString())
                    .child("dob").setValue(dob.text.toString())

                mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(uniqueKey.toString())
                    .child("status").setValue(status.text.toString())
                Toast.makeText(this@AddChildActivity, "تم اضافة الطفل", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}