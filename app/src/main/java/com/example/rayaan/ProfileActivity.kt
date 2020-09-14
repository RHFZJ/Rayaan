package com.example.rayaan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class ProfileActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etMobile:EditText
    lateinit var etEmail:EditText
    lateinit var btnEdit: Button
    internal var mDatabase: DatabaseReference? = null
    lateinit  var session:Session
    internal var flag = false
    internal var flag2 = false



    private val READ_STORAGE_CODE = 1001
    private val WRITE_STORAGE_CODE = 1002
    private val PICK_IMAGE_REQUEST = 234

    lateinit var selectImage: ImageView
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var filePath: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        session = Session(this)

        selectImage = findViewById(R.id.selectImage);



        etMobile = findViewById(R.id.etMobile)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnEdit = findViewById(R.id.btnEdit)
        mDatabase = FirebaseDatabase.getInstance().reference

        etMobile.setText(session.getMobile())
        etName.setText(session.getName())
        etEmail.setText(session.getEmail())

        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();

        var storage2 = FirebaseStorage.getInstance();
        var storageReference2= storage2!!.getReference()?.child("images/"+session.getImage());



        Glide.with(baseContext)
            .using(FirebaseImageLoader())
            .load(storageReference2)
            .placeholder(resources.getDrawable(R.drawable.logo))
            .into(selectImage)


        selectImage.setOnClickListener { showFileChooser() }



        btnEdit.setOnClickListener(View.OnClickListener {
            val query = mDatabase!!.child("Users").child(session.getId().toString())
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        flag = false
                        if (etName.getText().toString().isEmpty()) {
                            etName.setError("الرجاء ادخال الاسم")
                            flag = true
                        }

                        if (etMobile.getText().toString().isEmpty()) {
                            etMobile.setError("الرجاء ادخال رقم الجوال")
                            flag = true
                        } else {
                            if (etMobile.getText().toString().length != 10) {
                                etMobile.setError("الرجاء ادخال رقم جوال صحيح")
                                flag = true
                            } else if (etMobile.getText().toString().get(0) != '0' || etMobile.getText().toString().get(1) != '5') {
                                etMobile.setError("الرجاء ادخال رقم جوال صحيح")
                                flag = true
                            }
                        }
                        if (etEmail.getText().toString().isEmpty()) {
                            etEmail.setError("الرحاء ادخال البريد الالكتروني")
                            flag = true
                        } else {
                            val pattern: Pattern
                            val matcher: Matcher

                            val EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                                    "[a-zA-Z0-9_+&*-]+)*@" +
                                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                    "A-Z]{2,7}$"
                            pattern = Pattern.compile(EMAIL_PATTERN)
                            matcher = pattern.matcher(etEmail.getText().toString())
                            if (!matcher.matches()) {
                                etEmail.setError("الرحاء ادخال بريد الكتروني صحيح")
                                flag = true
                            }
                        }
                        if (!flag) {


                            if(flag2) {

                                var a = UUID.randomUUID().toString()
                                uploadImage(a)

                                Toast.makeText(this@ProfileActivity, "تم تعديل المستخدم", Toast.LENGTH_SHORT).show()
                                session.setName(etName.text.toString())
                                session.setMobile(etMobile.text.toString())
                                session.setEmail(etEmail.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("name")
                                    .setValue(etName.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("mobile")
                                    .setValue(etMobile.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("email")
                                    .setValue(etEmail.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("photo").setValue(a)
                                session.setImage(a)

                            }else{
                                Toast.makeText(this@ProfileActivity, "تم تعديل المستخدم", Toast.LENGTH_SHORT).show()
                                session.setName(etName.text.toString())
                                session.setMobile(etMobile.text.toString())
                                session.setEmail(etEmail.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("name")
                                    .setValue(etName.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("mobile")
                                    .setValue(etMobile.text.toString())
                                mDatabase!!.child("Users").child(session.getId().toString()).child("email")
                                    .setValue(etEmail.text.toString())
                            }

                            session.setName(etName.text.toString())
                            session.setMobile(etMobile.text.toString())
                            session.setEmail(etEmail.text.toString())
                            session.setName(etName.text.toString())

                        }


                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })

        })

    }

    private fun uploadImage(name:String) {

        if (filePath != null) {

            val ref = storageReference?.child("images/" + name)
            ref!!.putFile(filePath)
                .addOnSuccessListener {
                    Toast.makeText(this@ProfileActivity, "تم تسجيل المستخدم", Toast.LENGTH_SHORT).show()

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
                try {
                    imageStream = contentResolver.openInputStream(imageUri!!)
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    selectImage.setImageBitmap(selectedImage)
                    flag2 = true

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
