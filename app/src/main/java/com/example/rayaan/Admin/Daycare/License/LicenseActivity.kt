package com.example.rayaan.Admin.Daycare.License

import android.os.Bundle

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*

import java.util.*

class LicenseActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var lis: ArrayList<License>
    lateinit var adap: LicenseAdapter
    //gridLayoutManager = هي جزء من الريسايكل فيو تستخدم لعرض قائمة من البيانات في القرايد مثل استوديو الصور يكون مقسم عمود وصف
    private var gridLayoutManager: GridLayoutManager? = null
    var id:String = ""
    internal var mDatabase: DatabaseReference? = null
//super.onCreate= فايدتها ينفذ الكود بالكامل لو تجاهلته راح ينفذ فقط التعليمات
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
        lis = ArrayList<License>()
        adap = LicenseAdapter(lis,baseContext,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        getDAta()


    }

    private fun getDAta() {
        val query = mDatabase!!.child("Users").child(id).child("license")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        lis.add(License(user.key.toString()
                                ,user.child("photo").getValue().toString(),user.child("type").getValue().toString()))

                    }
                    //adap.notifyDataSetChanged()= فايدته لتحديث البيانات يعني يطلع لي اخر تحديث سويته لتراخيص
                    adap.notifyDataSetChanged()
                    if(lis.size == 0){
                        Toast.makeText(
                            this@LicenseActivity,
                            "لا يوجد شهادات",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                    Toast.makeText(
                        this@LicenseActivity,
                        "لا يوجد شهادات",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }
}
data class License(val id:String,val photo:String, val type: String)
