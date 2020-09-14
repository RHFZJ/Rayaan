package com.example.rayaan.Parent

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Specialist.ChatActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference

import java.util.ArrayList

class AdapterChat(private val list: ArrayList<SetGetChat>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    lateinit var session:Session
    internal var mDatabase: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = list.get(position).name
        holder.text.setOnClickListener(View.OnClickListener {

            a.startActivity(
                Intent(
                    a,
                    ChatActivity::class.java
                ).putExtra("sname",  list.get(position).key)
            )


        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val text: TextView


        init {

            text = itemView.findViewById(R.id.text) as TextView

        }
    }

}