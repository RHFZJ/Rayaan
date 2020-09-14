package com.example.rayaan.Daycare.Complaint

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Specialist.Ads.AdsActivity
import com.example.rayaan.R
import java.util.ArrayList



class CompAdapter(private val list: ArrayList<comp>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<CompAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_compliant_recycler_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if(!list.get(position).replay.equals("null")) {
//            holder.replay.setText(list.get(position).replay)
//        }else{
//            holder.replay.setText("لا يوجد رد")
//        }
        holder.desc.setText(list.get(position).desc)
        holder.date.setText(list.get(position).date)

        holder.itemView.setOnClickListener(View.OnClickListener {
            a.startActivity(
                Intent(a,
                    ComplaintReplayActivity::class.java).putExtra("id",list.get(position).id).putExtra("title",list.get(position).desc))
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

//        val replay: TextView
        val desc: TextView
        val date: TextView


        init {
            desc = itemView.findViewById(R.id.desc)
//            replay = itemView.findViewById(R.id.replay)
            date = itemView.findViewById(R.id.date)

        }
    }

}