package com.example.rayaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.rayaan.Admin.AdminMainActivity
import com.example.rayaan.Admin.ForgetPasswordActivity
import com.example.rayaan.Daycare.DaycareMainActivity
import com.example.rayaan.Parent.ParentMainActivity
import com.example.rayaan.Specialist.SpecialistMainActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_parent_main.*

class MainActivity : AppCompatActivity() {


    lateinit var username: EditText;
    lateinit var pasword:EditText
    lateinit var login: Button
    internal var flag = false
    internal var mDatabase: DatabaseReference? = null

    lateinit  var session:Session

    lateinit var signup:Button
    lateinit var forget: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = Session(this)

        username = findViewById(R.id.username)
        pasword = findViewById(R.id.password)
        login = findViewById(R.id.login)
        signup = findViewById(R.id.signup)
        forget = findViewById(R.id.forget)

        mDatabase = FirebaseDatabase.getInstance().reference

        if(session.getLoggedIn()){
            if(session.getType().equals("admin")){
                val i = Intent(baseContext, AdminMainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            }else if(session.getType().equals("daycare")) {
                val i = Intent(baseContext, DaycareMainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            }else if(session.getType().equals("specialist")) {
                val i = Intent(baseContext, SpecialistMainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            }else if(session.getType().equals("parent")) {
                val i = Intent(baseContext, ParentMainActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            }
        }
        signup.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext, ChooseUserActivity::class.java)

            startActivity(i)

        })
        forget.visibility = View.GONE
        forget.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext, ForgetPasswordActivity::class.java)

            startActivity(i)

        })
        login.setOnClickListener {
            flag = false
            val query = mDatabase!!.child("Users")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (user in dataSnapshot.children) {

                            if (user.child("username").value!!.toString() == username.text.toString() && user.child("password").value!!.toString() == pasword.getText().toString()) {
                                flag = true
                                session.setId(user.key.toString())
                                session.setMobile(user.child("mobile").value!!.toString())
                                session.setName(user.child("name").value!!.toString())
                                session.setLogin(true)
                                session.setEmail(user.child("email").value!!.toString())
                                session.setType(user.child("type").value!!.toString())
                                session.setUsername(username.text.toString())

                                if(user.child("photo").exists()) {
                                    session.setImage(user.child("photo").value!!.toString())
                                }

                                Toast.makeText(this@MainActivity, "تم تسجيل الدخول", Toast.LENGTH_SHORT).show()
                                if(user.child("type").value!!.toString().equals("admin")) {
                                    val i = Intent(baseContext, AdminMainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                    finish()
                                }else if(user.child("type").value!!.toString().equals("daycare")) {
                                    val i = Intent(baseContext, DaycareMainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                    finish()
                                }else if(user.child("type").value!!.toString().equals("specialist")) {
                                    val i = Intent(baseContext, SpecialistMainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                    finish()
                                }else if(user.child("type").value!!.toString().equals("parent")) {
                                    val i = Intent(baseContext,ParentMainActivity::class.java)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                    finish()
                                }
                                break


                            }
                        }
                        if (!flag) {
                            Toast.makeText(
                                this@MainActivity,
                                "اسم المستخدم او كلمة المرور غير صحيحة",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }
}
