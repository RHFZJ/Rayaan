package com.example.rayaan.Daycare.WorkingHours

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class WorkingAdapter(private val list: ArrayList<Working>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<WorkingAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.working_daycare_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.from.setText(list.get(position).time.split("-")[0])
        holder.To.setText(list.get(position).time.split("-")[1])
        holder.DAy.setText(list.get(position).day)
        holder.delete.setOnClickListener(View.OnClickListener {

            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase!!.child("Users").child(session.getId().toString()).child("workinghour").child(list.get(position).id).removeValue()
            list.removeAt(position)
            notifyDataSetChanged()
        })
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val from: TextView
        val To: TextView
        val DAy: TextView
        var delete: ImageView
        init {
            from = itemView.findViewById(R.id.from)
            To = itemView.findViewById(R.id.to)
            DAy = itemView.findViewById(R.id.day)
            delete = itemView.findViewById(R.id.delete)
        }
    }

}