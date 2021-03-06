package com.example.rayaan.Parent.Specialist;

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rayaan.R
import com.example.rayaan.Session
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage

import java.util.ArrayList

class SpecialistAdapter(private val list: ArrayList<DayCare>, private val context: Activity, private val a: Activity)
    : RecyclerView.Adapter<SpecialistAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_spec_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.setText(list.get(position).name)
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+list.get(position).photo);
        Glide.with(a)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(a.resources.getDrawable(R.drawable.img))
            .into(holder.image)
        holder.itemView.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                SpecialistDetailsActivity::class.java).putExtra("id",list.get(position).id))


        })
        holder.image.setOnClickListener(View.OnClickListener {
            context.startActivity(Intent(context,
                SpecialistDetailsActivity::class.java).putExtra("id",list.get(position).id).putExtra("username",list.get(position).username))


        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val name: TextView
        val image: ImageView


        init {

            name = itemView.findViewById(R.id.name)
            image = itemView.findViewById(R.id.image)


        }
    }

}