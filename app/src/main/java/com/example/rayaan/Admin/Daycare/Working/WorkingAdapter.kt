package com.example.rayaan.Admin.Daycare.Working

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R

import java.util.ArrayList

class WorkingAdapter(private val list: ArrayList<Working>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<WorkingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.working_recycler_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.setText(list.get(position).time)
        holder.day.setText(list.get(position).day)
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val time: TextView
        val day: TextView

        init {
            time = itemView.findViewById(R.id.time)
            day = itemView.findViewById(R.id.day)
        }
    }

}