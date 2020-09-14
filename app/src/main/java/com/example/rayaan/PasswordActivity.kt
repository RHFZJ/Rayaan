package com.example.rayaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*

class PasswordActivity : AppCompatActivity() {


    lateinit var oldpass: EditText
    lateinit var newpass: EditText
    lateinit var changePassword: Button
    lateinit var session: Session
    internal var mDatabase: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        mDatabase = FirebaseDatabase.getInstance().reference

        oldpass = findViewById(R.id.etoldPassword) as EditText
        newpass = findViewById(R.id.etnewPassword) as EditText
        changePassword = findViewById(R.id.btnChange) as Button
        session = Session(baseContext)
        changePassword.setOnClickListener { ChangePassword() }
    }

    private fun ChangePassword()
    {
        if (oldpass.text.toString().isEmpty()) {
            oldpass.error = "الرجاء ادخال كلمة المرور السابقة"
        } else if (newpass.text.toString().isEmpty()) {
            newpass.error = "الرجاء ادخال كلمة المرور الجديدة"
        } else {
            if (newpass.text.toString().length < 8) {
                newpass.error = "الرجاء ادخال كلمة المرور يجب ان تحتوي على 8 احرف على الاقل"
                return
            }
            if (newpass.text.toString() == newpass.text.toString().toLowerCase()) {
                newpass.error = "كلمة المرور يجب ان تحتوي على احرف كبيرة وصغيرة"
                return
            }
            val query = mDatabase!!.child("Users").child(session.getId().toString())
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {

                        if(dataSnapshot.child("password").getValue().toString().equals(oldpass.text.toString())){
                            mDatabase!!.child("Users").child(session.getId().toString()).child("password").setValue(newpass.text.toString())
                            Toast.makeText(this@PasswordActivity, "تم تعديل كلمة المرور", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(this@PasswordActivity, "كلمة المرور السابقة غير صحيحة!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }
}
