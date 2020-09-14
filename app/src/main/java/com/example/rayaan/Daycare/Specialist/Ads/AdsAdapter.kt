package com.example.rayaan.Daycare.Specialist.Ads

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage




class AdsAdapter(private val list: ArrayList<Ads>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<AdsAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session:Session

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ads_recycler_row, parent, false)
        session = Session(a)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.setText(list.get(position).title)
        holder.content.setText(list.get(position).content)
        holder.date.setText(list.get(position).date)
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+list.get(position).photo);
        Glide.with(a)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(a.resources.getDrawable(R.drawable.img))
            .into(holder.image)

        holder.delete.visibility = View.GONE
        holder.edit.visibility = View.GONE

    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView
        val date: TextView
        val content: TextView
        var image: ImageView

        var edit: Button
        var delete: Button

        init {
            title = itemView.findViewById(R.id.title)
            date = itemView.findViewById(R.id.date)
            content = itemView.findViewById(R.id.content)
            image = itemView.findViewById(R.id.image)
            edit = itemView.findViewById(R.id.edit)
            delete = itemView.findViewById(R.id.delete)

        }
    }

}