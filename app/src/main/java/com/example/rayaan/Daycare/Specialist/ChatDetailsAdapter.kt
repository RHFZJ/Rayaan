package com.example.rayaan.Daycare.Specialist;

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference

import java.util.ArrayList

class ChatDetailsAdapter(private val list: ArrayList<SetGetChatDetails>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<ChatDetailsAdapter.ViewHolder>() {

    lateinit var session:Session
    internal var mDatabase: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chatdetails, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            if (list.get(position).name.equals(session.getUsername())) {
                holder.dateTime.setGravity(Gravity.LEFT)
                holder.messageText.setGravity(Gravity.LEFT)
                holder.name.gravity = Gravity.LEFT
            } else {
                holder.dateTime.setGravity(Gravity.RIGHT)
                holder.messageText.setGravity(Gravity.RIGHT)
                holder.name.gravity = Gravity.RIGHT
            }

        holder.dateTime.setText(list.get(position).dateTime)
        holder.messageText.setText(list.get(position).chatText)
        holder.name.setText(list.get(position).name)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val messageText: TextView
        val name: TextView
        val dateTime: TextView


        init {
            dateTime = itemView.findViewById(R.id.dateTime) as TextView
            name = itemView.findViewById(R.id.name)
            messageText = itemView.findViewById(R.id.text) as TextView

        }
    }

}