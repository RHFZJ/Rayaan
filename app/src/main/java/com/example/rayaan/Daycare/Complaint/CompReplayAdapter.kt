package com.example.rayaan.Daycare.Complaint

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.R
import com.example.rayaan.Session
import java.util.ArrayList



class CompReplayAdapter(private val list: ArrayList<complReplay>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<CompReplayAdapter.ViewHolder>() {
    lateinit var session:Session

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_compliant_replay_recycler_row, parent, false)
        session = Session(a)

        return ViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        if(!list.get(position).replay.equals("null")) {
//            holder.replay.setText(list.get(position).replay)
//        }else{
//            holder.replay.setText("لا يوجد رد")
//        }
        if(session.getType().equals("admin")) {
            if (list.get(position).flag.equals("admin")) {
                holder.desc.textDirection = View.TEXT_DIRECTION_LTR
                holder.date.textDirection = View.TEXT_DIRECTION_LTR
            }else{
                holder.desc.textDirection = View.TEXT_DIRECTION_RTL
                holder.date.textDirection = View.TEXT_DIRECTION_RTL
            }
        }else{
            if (list.get(position).flag.equals("user")) {

                holder.desc.textDirection = View.TEXT_DIRECTION_RTL
                holder.date.textDirection = View.TEXT_DIRECTION_RTL

            }else{
                holder.desc.textDirection = View.TEXT_DIRECTION_LTR
                holder.date.textDirection = View.TEXT_DIRECTION_LTR
            }
        }
        holder.desc.setText(list.get(position).text)
        holder.date.setText(list.get(position).date)



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