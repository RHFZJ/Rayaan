package com.example.rayaan.Daycare.License

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.rayaan.R
import java.io.FileNotFoundException
import java.io.InputStream
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.rayaan.Session
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.*
import com.weiwangcn.betterspinner.library.BetterSpinner
import java.util.*


class EditLicenseActivity : AppCompatActivity() {

    private val READ_STORAGE_CODE = 1001
    private val WRITE_STORAGE_CODE = 1002
    private val PICK_IMAGE_REQUEST = 234

    lateinit var selectImage:ImageView
    lateinit var type:BetterSpinner
    lateinit var add:Button
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var filePath: Uri
    internal var mDatabase: DatabaseReference? = null
    lateinit var session: Session
    var id:String = ""
    var flag = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_license)

        val i = intent
        val b = i.extras
        if (b != null) {
            id = b.getString("id").toString()
        }



        selectImage = findViewById(R.id.selectImage);
        type = findViewById(R.id.type);
        add = findViewById(R.id.add)
        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();
        selectImage.setOnClickListener { showFileChooser() }
        session = Session(baseContext)

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.type, android.R.layout.simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        type.setAdapter(adapter)
        mDatabase = FirebaseDatabase.getInstance().reference
        add.setOnClickListener(View.OnClickListener {
            if(type.text.toString().isEmpty()){
                type.setError("الرجاء اختيار النوع")
            }else {

                if(flag) {
                    var a = UUID.randomUUID().toString()
                    uploadImage(a)
                    mDatabase!!.child("Users").child(session.getId().toString()).child("license")
                        .child(id).child("photo").setValue(a)
                }

                mDatabase!!.child("Users").child(session.getId().toString()).child("license")
                    .child(id).child("type").setValue(type.text.toString())
                Toast.makeText(this@EditLicenseActivity, "تم تعديل الشهادة", Toast.LENGTH_SHORT).show()
                finish()

            }


        })
        getDAta()

    }

    private fun getDAta() {
        val query = mDatabase!!.child("Users").child(session.getId().toString()).child("license").child(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                    type.setText(dataSnapshot.child("type").getValue().toString())
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


//                    Toast.makeText(this@AddAdsActivity, "Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->

//                    Toast.makeText(this@AddAdsActivity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener { taskSnapshot ->
//                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
//                        .totalByteCount
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
                try {
                    imageStream = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    selectImage.setImageBitmap(selectedImage)
                    flag = true

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
