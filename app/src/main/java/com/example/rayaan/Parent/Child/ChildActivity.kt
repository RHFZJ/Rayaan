package com.example.rayaan.Parent.Child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import java.util.ArrayList

class ChildActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var session: Session
    lateinit var userslist: ArrayList<Child>
    lateinit var adap: ChildAdapter
    internal var mDatabase: DatabaseReference? = null
    private var gridLayoutManager: GridLayoutManager? = null
    lateinit var add: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        userslist = ArrayList<Child>()
        adap = ChildAdapter(userslist,this,this)
        gridLayoutManager = GridLayoutManager(this, 1)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = adap
        add =findViewById(R.id.add);
        add.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, AddChildActivity::class.java))
        })
    }

    override fun onResume() {
        super.onResume()
        getChild()

    }
    private fun getChild() {
        userslist.clear()
        val query = mDatabase!!.child("Users").child(session.getId().toString()).child("Child")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                            userslist.add(
                                Child(user.key.toString()
                                    ,user.child("name").getValue().toString(),user.child("dob").getValue().toString()
                                    ,user.child("status").getValue().toString())
                            )

                    }
                    adap.notifyDataSetChanged()
                    if(userslist.size == 0){
                        Toast.makeText(
                            this@ChildActivity,
                            "لا يوجد اطفال",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }else{
                        Toast.makeText(
                            this@ChildActivity,
                            "لا يوجد اطفال",
                            Toast.LENGTH_SHORT
                        ).show()

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
data class Child(val id:String,val name:String , val birthdate: String, val status: String)

