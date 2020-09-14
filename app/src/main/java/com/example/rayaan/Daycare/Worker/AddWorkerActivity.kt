package com.example.rayaan.Daycare.Worker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.weiwangcn.betterspinner.library.BetterSpinner

class AddWorkerActivity : AppCompatActivity() {


    lateinit var name: EditText
    lateinit var specialist: EditText
    lateinit var status: BetterSpinner
    lateinit var add: Button

    lateinit var session: Session
    internal var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_worker)


        name = findViewById(R.id.name);
        specialist = findViewById(R.id.specialist);
        status = findViewById(R.id.status);
        add = findViewById(R.id.add);

        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.statuss, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        status.setAdapter(adapter)

        add.setOnClickListener(View.OnClickListener {
            if (name.text.toString().isEmpty()) {
                name.setError("الرجاء ادخال الاسم")
            } else if (specialist.text.toString().isEmpty()) {
                specialist.setError("الرجاء ادخال الاختصاص")
            }else if (status.text.toString().isEmpty()) {
                status.setError("الرجاء تحديد الحالة")
            } else {


                val uniqueKey = mDatabase!!.child("Users").push().key
                mDatabase!!.child("Users").child(session.getId().toString()).child("worker").child(uniqueKey.toString())
                    .child("name").setValue(name.text.toString())

                mDatabase!!.child("Users").child(session.getId().toString()).child("worker").child(uniqueKey.toString())
                    .child("specialist").setValue(specialist.text.toString())

                mDatabase!!.child("Users").child(session.getId().toString()).child("worker").child(uniqueKey.toString())
                    .child("type").setValue(status.text.toString())
                Toast.makeText(this@AddWorkerActivity, "تم اضافة الموظف", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}
