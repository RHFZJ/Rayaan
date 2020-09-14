package com.example.rayaan.Admin.Daycare.Complaint

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Complaint.ComplaintReplayActivity
import com.example.rayaan.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class CompAdapter(private val list: ArrayList<comp>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<CompAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_compliant_recycler_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if(!list.get(position).replay.equals("null")) {
//            holder.repla.setText(list.get(position).replay)
//            holder.replaytext.setText(list.get(position).replay)
//        }else{
//            holder.repla.setText("لا يوجد رد")
//        }
        holder.desc.setText(list.get(position).desc)
        holder.date.setText(list.get(position).date)
        holder.name.setText(list.get(position).username)

        holder.itemView.setOnClickListener(View.OnClickListener {
            a.startActivity(
                Intent(a,
                    ComplaintReplayActivity::class.java).putExtra("id",list.get(position).id).putExtra("title",list.get(position).desc))
        })

//        holder.send.setOnClickListener(View.OnClickListener {
//            mDatabase = FirebaseDatabase.getInstance().reference
//            mDatabase!!.child("Complaint").child(list.get(position).id).child("replay").setValue(holder.replaytext.text.toString())
//            list.get(position).replay = holder.replaytext.text.toString()
//            notifyDataSetChanged()
//
//        })


    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        val repla: TextView
        val desc: TextView
        val date: TextView
        val name :TextView
//        val send:Button
//        val  replaytext:EditText
//


        init {
            desc = itemView.findViewById(R.id.desc)
//            repla = itemView.findViewById(R.id.replay)
            date = itemView.findViewById(R.id.date)
            name = itemView.findViewById(R.id.name)
//            send = itemView.findViewById(R.id.send)
//            replaytext = itemView.findViewById(R.id.replaytext)

        }
    }

}