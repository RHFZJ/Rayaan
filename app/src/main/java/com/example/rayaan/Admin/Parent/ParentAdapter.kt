package com.example.rayaan.Admin.Parent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rayaan.Admin.Daycare.License.LicenseActivity
import com.example.rayaan.Admin.Daycare.Working.WorkingHourActivity
import com.example.rayaan.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.util.ArrayList

class ParentAdapter(private val list: ArrayList<DayCare>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {

    internal var mDatabase: DatabaseReference? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.admin_parent_recycler_row, parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.setText(list.get(position).username)
        holder.mobile.setText(list.get(position).mobile)
        holder.email.setText(list.get(position).mail)
        holder.password.setText(list.get(position).pass)
        holder.name.setText(list.get(position).name)



        holder.mobile.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + list.get(position).mobile)
            a.startActivity(intent)
        }
        holder.delete.setOnClickListener(View.OnClickListener {
            mDatabase = FirebaseDatabase.getInstance().reference
            mDatabase!!.child("Users").child(list.get(position).id).removeValue()
            list.removeAt(position)
            notifyDataSetChanged()
        })



    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val password: TextView
        val username: TextView
        val name: TextView
        val mobile: TextView
        val email: TextView
        val delete: Button

        init {
            username = itemView.findViewById(R.id.username)
            password = itemView.findViewById(R.id.password)
            name = itemView.findViewById(R.id.name)
            mobile= itemView.findViewById(R.id.mobile)
            email = itemView.findViewById(R.id.mail)
            delete = itemView.findViewById(R.id.delete)



        }
    }

}