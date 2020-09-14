package com.example.rayaan.Daycare.Worker;

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Daycare.Specialist.Ads.AdsActivity
import com.example.rayaan.Daycare.Specialist.ChatActivity
import com.example.rayaan.Daycare.Specialist.WokingHours.WorkingHourActivity
import com.example.rayaan.R
import com.example.rayaan.Session
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class WorkerAdapter(private val list: ArrayList<DayCare>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<WorkerAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.daycare_worker_specialist_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.setText(list.get(position).username)
        holder.mobile.setText(list.get(position).mobile)
        holder.email.setText(list.get(position).mail)
        holder.name.setText(list.get(position).name)
        if(!list.get(position).price.equals("no")){
            holder.price.setText(list.get(position).price)
        }else{
            holder.price.visibility = View.GONE
            holder.price2.visibility = View.GONE
        }
        holder.mobile.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + list.get(position).mobile)
            a.startActivity(intent)
        }

        holder.chat.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                ChatActivity::class.java).putExtra("sname",list.get(position).username + "-" +session.getUsername()))
        })

        holder.ads.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                AdsActivity::class.java).putExtra("id",list.get(position).sid))
        })
        holder.workinghours.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                WorkingHourActivity::class.java).putExtra("id",list.get(position).sid))
        })
        holder.delete.setOnClickListener(View.OnClickListener {
            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase!!.child("Users").child(session.getId().toString()).child("worker")
                .child(list.get(position).id).removeValue()

            list.removeAt(position)
            notifyDataSetChanged()

            Toast.makeText(context, "تم حذف الموظف", Toast.LENGTH_SHORT).show()
        })

        if(list.get(position).pass.equals("")){
            //  holder.delete.visibility = View.GONE
            holder.workinghours.visibility = View.GONE
            holder.ads.visibility = View.GONE
            holder.chat.visibility = View.GONE
            holder.price.visibility = View.GONE
            holder.price2.visibility = View.GONE
            holder.price2.visibility = View.GONE

            holder.email.visibility = View.GONE
            holder.email2.visibility = View.GONE
            holder.username2.setText("التخصص:")
            holder.mobile2.setText("الوظيفة:")
        }


    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val username: TextView
        val username2: TextView
        val mobile2:TextView

        val name: TextView
        val mobile: TextView
        val email: TextView
        val email2:TextView
        val chat: Button
        val ads: Button
        val workinghours: Button
        val delete: Button

        val price: TextView
        val price2: TextView


        init {
            username = itemView.findViewById(R.id.username)
            username2 = itemView.findViewById(R.id.username2)

            name = itemView.findViewById(R.id.name)
            mobile= itemView.findViewById(R.id.mobile)
            mobile2= itemView.findViewById(R.id.mobile2)

            email = itemView.findViewById(R.id.mail)
            email2 = itemView.findViewById(R.id.mail2)

            price = itemView.findViewById(R.id.price)
            price2 = itemView.findViewById(R.id.price2)

            chat = itemView.findViewById(R.id.chat)
            ads = itemView.findViewById(R.id.ads)
            workinghours = itemView.findViewById(R.id.workinghours)
            delete = itemView.findViewById(R.id.delete)


        }
    }

}