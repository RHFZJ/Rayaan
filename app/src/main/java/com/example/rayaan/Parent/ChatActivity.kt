package com.example.rayaan.Parent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import java.util.ArrayList

class ChatActivity : AppCompatActivity() {

    lateinit var listView: RecyclerView
    val name: String = " "
    lateinit var gridLayoutManager: StaggeredGridLayoutManager
    lateinit var adapterChatDetails: AdapterChat
    lateinit var arrayList: ArrayList<SetGetChat>
    lateinit var session: Session
    internal var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)
        listView = findViewById(R.id.listView)
        mDatabase = FirebaseDatabase.getInstance().reference
        gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        arrayList = ArrayList<SetGetChat>()
        adapterChatDetails = AdapterChat(arrayList, this,this)
        listView.setAdapter(adapterChatDetails)
        listView.setLayoutManager(gridLayoutManager)
        listView.setAdapter(adapterChatDetails)
        session = Session(baseContext)
        val query = mDatabase!!.child("Chat")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(user.key.toString().split("-")[1].equals(session.getUsername().toString())
                            ||user.key.toString().split("-")[0].equals(session.getUsername().toString())){


                            if(user.key.toString().split("-")[1].equals(session.getUsername().toString())) {
                                arrayList.add(
                                    SetGetChat(
                                        user.key.toString(),
                                        user.key.toString().split("-")[0]
                                    )
                                )
                            }else if(user.key.toString().split("-")[0].equals(session.getUsername().toString())) {

                                arrayList.add(
                                    SetGetChat(
                                        user.key.toString(),
                                        user.key.toString().split("-")[1]
                                    )
                                )
                            }


                        }



//                        if(session.getType().equals("parent")){
//                            if(user.key.toString().split("-")[1].equals(session.getUsername())) {
//                                arrayList.add(
//                                    SetGetChat(
//                                        user.key.toString(),
//                                        user.key.toString().split("-")[0]
//                                    )
//                                )
//                            }
//                        }else if(session.getType().equals("daycare")){
//                            if(user.key.toString().split("-")[1].equals(session.getUsername())) {
//                                arrayList.add(
//                                    SetGetChat(
//                                        user.key.toString(),
//                                        user.key.toString().split("-")[0]
//                                    )
//                                )
//                            }
//                        }else{
//                            if(user.key.toString().split("-")[0].equals(session.getUsername())) {
//                                arrayList.add(
//                                    SetGetChat(
//                                        user.key.toString(),
//                                        user.key.toString().split("-")[1]
//                                    )
//                                )
//                            }
//                        }
                    }
                    adapterChatDetails.notifyDataSetChanged()
                    if(arrayList.size == 0){
                        Toast.makeText(
                            this@ChatActivity,
                            "لا يوجد محادثات",
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
data class SetGetChat(var key:String, var name:String)
