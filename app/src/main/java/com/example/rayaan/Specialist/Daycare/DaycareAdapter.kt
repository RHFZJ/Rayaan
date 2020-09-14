package com.example.rayaan.Specialist.Daycare;

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Specialist.Ads.AdsActivity
import com.example.rayaan.Daycare.Specialist.ChatActivity
import com.example.rayaan.Daycare.Specialist.WokingHours.WorkingHourActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference

import java.util.ArrayList

class DaycareAdapter(private val list: ArrayList<DayCare>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<DaycareAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.specialist_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.setText(list.get(position).username)
        holder.mobile.setText(list.get(position).mobile)
        holder.email.setText(list.get(position).mail)
      //  holder.password.setText(list.get(position).pass)
        holder.name.setText(list.get(position).name)
        holder.mobile.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + list.get(position).mobile)
            a.startActivity(intent)
        }

        if(!list.get(position).price.equals("no")){
            holder.price.setText(list.get(position).price)
        }else{
            holder.price.visibility = View.GONE
            holder.price2.visibility = View.GONE
        }

        holder.chat.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                ChatActivity::class.java).putExtra("sname",  session.getId() +"-"+list.get(position).id))
        })

        holder.ads.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                AdsActivity::class.java).putExtra("id",list.get(position).id))
        })
        holder.workinghours.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                WorkingHourActivity::class.java).putExtra("id",list.get(position).id))
        })

    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //val password: TextView
        val username: TextView
        val name: TextView
        val mobile: TextView
        val email: TextView
        val chat: Button
        val ads: Button
        val workinghours: Button
        val price: TextView
        val price2: TextView

        init {
            username = itemView.findViewById(R.id.username)
          //  password = itemView.findViewById(R.id.password)
            name = itemView.findViewById(R.id.name)
            mobile= itemView.findViewById(R.id.mobile)
            email = itemView.findViewById(R.id.mail)
            chat = itemView.findViewById(R.id.chat)
            ads = itemView.findViewById(R.id.ads)
            workinghours = itemView.findViewById(R.id.workinghours)

            price = itemView.findViewById(R.id.price)
            price2 = itemView.findViewById(R.id.price2)
        }
    }

}