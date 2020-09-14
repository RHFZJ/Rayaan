package com.example.rayaan.Parent.Child;

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class ChildAdapter(private val list: ArrayList<Child>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.child_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.status.setText(list.get(position).status)
        holder.date.setText(list.get(position).birthdate)
        holder.name.setText(list.get(position).name)
        holder.delete.setOnClickListener(View.OnClickListener {
            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase!!.child("Users").child(session.getId().toString()).child("Child").child(list.get(position).id).removeValue()
            list.removeAt(position)
            notifyDataSetChanged()
        })

        holder.edit.setOnClickListener(View.OnClickListener {
            a.startActivity(
                Intent(a,
                    EditChildActivity::class.java).putExtra("id",list.get(position).id))


        })


    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val name: TextView
        val date: TextView
        val status: TextView
        val delete: ImageView
        val edit: ImageView


        init {

            name = itemView.findViewById(R.id.name)
            date= itemView.findViewById(R.id.date)
            status = itemView.findViewById(R.id.status)
            delete = itemView.findViewById(R.id.delete)
            edit = itemView.findViewById(R.id.edit)


        }
    }

}