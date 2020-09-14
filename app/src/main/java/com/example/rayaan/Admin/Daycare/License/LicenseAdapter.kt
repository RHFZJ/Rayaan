package com.example.rayaan.Admin.Daycare.License

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rayaan.R
import com.example.rayaan.Session
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.FirebaseStorage

import java.util.ArrayList

class LicenseAdapter(private val list: ArrayList<License>, private val context: Context, private val a: Activity)
    : RecyclerView.Adapter<LicenseAdapter.ViewHolder>() {
    internal var mDatabase: DatabaseReference? = null
    lateinit var session: Session

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_license_recycler_row, parent, false)
        session = Session(context)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.type.setText(list.get(position).type)
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage!!.getReference()?.child("images/"+list.get(position).photo);
        Glide.with(a)
            .using(FirebaseImageLoader())
            .load(storageReference)
            .placeholder(a.resources.getDrawable(R.drawable.img))
            .into(holder.image)

    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val type: TextView
        var image: ImageView

        init {
            type = itemView.findViewById(R.id.type)
            image = itemView.findViewById(R.id.image)
        }
    }

}