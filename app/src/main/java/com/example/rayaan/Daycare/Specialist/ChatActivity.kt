package com.example.rayaan.Daycare.Specialist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rayaan.Daycare.Specialist.Ads.AdsActivity
import com.example.rayaan.Daycare.Specialist.Child.ChildActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_parent_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    lateinit var btn_send_msg: Button
    lateinit var input_msg: EditText
    lateinit var obj: SetGetChatDetails
    lateinit var arrayList: ArrayList<SetGetChatDetails>
    lateinit var recyclerView: RecyclerView
    private var room_name: String? = null
    private var root: DatabaseReference? = null
    private var temp_key: String? = null
    lateinit var dateFormat: SimpleDateFormat
    internal var flag: Boolean = false
    lateinit var gridLayoutManager: StaggeredGridLayoutManager
    internal var backflag: Boolean = false
    lateinit var adapterChatDetails: ChatDetailsAdapter
    lateinit var session: Session
    lateinit var childs:Button
    internal var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mDatabase = FirebaseDatabase.getInstance().reference
        recyclerView = findViewById(R.id.listView)
        gridLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        arrayList = ArrayList()
        adapterChatDetails = ChatDetailsAdapter(arrayList, this,this)

        recyclerView.adapter = adapterChatDetails
        recyclerView.layoutManager = gridLayoutManager
        session = Session(this)
        btn_send_msg = findViewById(R.id.btn_send)
        input_msg = findViewById(R.id.msg_input)
        childs = findViewById(R.id.childs);

        dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val i = intent
        val b = i.extras
        if (b != null) {
            room_name = b!!.getString("sname")
            root = FirebaseDatabase.getInstance().reference.child("Chat").child(room_name.toString())
        }
        btn_send_msg.setOnClickListener(View.OnClickListener {
            if (!input_msg.getText().toString().isEmpty()) {
                flag = true
                val map = HashMap<String, Any>()
                temp_key = root!!.push().key
                root!!.updateChildren(map)
                val message_root = root!!.child(temp_key.toString())
                val map2 = HashMap<String, Any>()
                map2["name"] = session.getUsername().toString()
                map2["msg"] = input_msg.getText().toString()
                map2["DateTime"] = dateFormat.format(Date(System.currentTimeMillis()))
                message_root.updateChildren(map2)
                input_msg.setText("")

            }
        })
        childs.visibility = View.GONE


        val query = mDatabase!!.child("Users")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (user in dataSnapshot.children) {
                        if(room_name?.split("-")!![1].equals(user.child("username").value!!.toString())) {
                            if ((user.child("type").value!!.toString() == "parent")) {
                                if(session.getType().equals("parent")){
                                    childs.visibility = View.GONE
                                }else{
                                    childs.visibility = View.VISIBLE
                                }
                                break;

                            }else{
                                childs.visibility = View.GONE
                            }
                        }
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


        childs.setOnClickListener(View.OnClickListener {
            startActivity(Intent(baseContext, ChildActivity::class.java)
                .putExtra("username", room_name?.split("-")!![1]))


            Log.d("aboooooooood",room_name?.split("-")!![1])
        })

        root!!.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {

                append_chat_conversation(dataSnapshot)
                //update(dataSnapshot);


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                if (flag) {
                    flag = false
                } else {
                    append_chat_conversation(dataSnapshot)

                }


            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }


    private fun append_chat_conversation(dataSnapshot: DataSnapshot) {
        val i = dataSnapshot.children.iterator()


        while (i.hasNext()) {

            obj = SetGetChatDetails((((i.next() as DataSnapshot).value as String?)!!),(((i.next() as DataSnapshot).value as String?)!!),((i.next() as DataSnapshot).value.toString()))
            var aa = arrayOfNulls<String>(0)
            aa = room_name!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            if (!obj.name.equals(session.getName()) && !backflag) {
                flag = true

            } else {
//                val a = (i.next() as DataSnapshot).value as String?
            }
            //   if(!flag){
            arrayList.add(obj)

            // }

        }
        adapterChatDetails.notifyDataSetChanged()

        recyclerView.smoothScrollToPosition(arrayList.size)

    }
}
data class SetGetChatDetails(var dateTime:String, var chatText:String, var name: String)