package com.example.rayaan.Daycare.Ads

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rayaan.R
import java.io.FileNotFoundException
import java.io.InputStream
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.widget.Toast
import android.net.Uri
import android.view.View
import android.widget.Button
import com.bumptech.glide.Glide
import com.example.rayaan.Session
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.*
import java.util.*


class EditAdsActivity : AppCompatActivity() {

    private val READ_STORAGE_CODE = 1001
    private val WRITE_STORAGE_CODE = 1002
    private val PICK_IMAGE_REQUEST = 234

    lateinit var selectImage:ImageView
    lateinit var title:EditText
    lateinit var edit:Button
    lateinit var content:EditText
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var filePath: Uri
    internal var mDatabase: DatabaseReference? = null
    lateinit var session: Session
    var flag = false
    var id:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_ads)

        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }


        selectImage = findViewById(R.id.selectImage);
        title = findViewById(R.id.title);
        edit = findViewById(R.id.edit)
        content = findViewById(R.id.content);
        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();
        selectImage.setOnClickListener { showFileChooser() }
        session = Session(baseContext)
        mDatabase = FirebaseDatabase.getInstance().reference
        edit.setOnClickListener(View.OnClickListener {
            if(title.text.toString().isEmpty()){
                title.setError("الرجاء اخال العنوان")
            }else if(content.text.toString().isEmpty()){
                content.setError("الرجاء اخال المحتوى")
            }else {



                if(flag) {
                    var a = UUID.randomUUID().toString()
                    uploadImage(a)
                    mDatabase!!.child("Ads").child(id)
                        .child("photo").setValue(a)
                }
                mDatabase!!.child("Ads").child(id)
                    .child("content").setValue(content.text.toString())

                mDatabase!!.child("Ads").child(id)
                    .child("title").setValue(title.text.toString())

                mDatabase!!.child("Ads").child(id)
                    .child("userid").setValue(session.getId().toString())

                Toast.makeText(this@EditAdsActivity, "تم تعديل الاعلان", Toast.LENGTH_SHORT).show()
                finish()

            }
        })
        getDAta()

    }

    private fun getDAta() {
        val query = mDatabase!!.child("Ads").child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    title.setText(dataSnapshot.child("title").getValue().toString())
                    content.setText(dataSnapshot.child("content").getValue().toString())

                    var storage1 = FirebaseStorage.getInstance();
                    var storageReference2 = storage1!!.getReference()?.child("images/"+dataSnapshot.child("photo").getValue().toString());

                    Glide.with(baseContext)
                        .using(FirebaseImageLoader())
                        .load(storageReference2)
                        .placeholder(resources.getDrawable(R.drawable.img))
                        .into(selectImage)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
    private fun uploadImage(name:String) {

        if (filePath != null) {

            val ref = storageReference?.child("images/" + name)
            ref!!.putFile(filePath)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                }
                .addOnProgressListener { taskSnapshot ->
                }
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == PICK_IMAGE_REQUEST) {

                filePath = data!!.data!!

                val imageUri = data!!.data
                val imageStream: InputStream?
                flag = true
                try {
                    imageStream = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    selectImage.setImageBitmap(selectedImage)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }

        }
    }



    private fun showFileChooser() {

        if (isPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_STORAGE_CODE)

        }
    }
    private fun isPermissionGranted(permission: String): Boolean {
        val result = ContextCompat.checkSelfPermission(this, permission)

        return if (result == PackageManager.PERMISSION_GRANTED) true else false
    }


    private fun requestPermission(permission: String, code: Int) {

        ActivityCompat.requestPermissions(this, arrayOf(permission), code)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if (requestCode == READ_STORAGE_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                finish()
            }
        } else if (requestCode == WRITE_STORAGE_CODE) {


            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                finish()
            }
        }
    }
}
