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
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private val READ_STORAGE_CODE = 1001
    private val WRITE_STORAGE_CODE = 1002
    private val PICK_IMAGE_REQUEST = 234

    lateinit var selectImage: ImageView
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    lateinit var filePath: Uri
    internal var mDatabase: DatabaseReference? = null
//lateinit = وظيفتها تجلب المعلومات وتنقلها من صفحة لصفحة
    lateinit var name:EditText
    lateinit var username:EditText
    lateinit var mobile:EditText
    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var cpassword:EditText
    lateinit var btnRegister:Button

    var flag = false

    var type:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val i = intent
        val b = i.extras
        if (b != null) {
            type = b.getString("type").toString()
        }


        selectImage = findViewById(R.id.selectImage);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        btnRegister = findViewById(R.id.btnRegister);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        storage = FirebaseStorage.getInstance();
        storageReference = storage!!.getReference();
        mDatabase = FirebaseDatabase.getInstance().reference
//showFileChooser= لما اضغط عشان اختار صورة يجيني مثل الاكسس يسمح لي ادخل الاستوديو
        selectImage.setOnClickListener { showFileChooser() }


        btnRegister.setOnClickListener(View.OnClickListener {

            flag = false
            if (name.text.toString().isEmpty()) {
                name.error = "الرجاء ادخال الاسم"
                flag = true
            }
            flag = false
            if (username.text.toString().isEmpty()) {
                username.error = "الرجاء ادخال اسم المستخدم"
                flag = true
            }

            if (mobile.text.toString().isEmpty()) {
                mobile.error = "الرجاء ادخال رقم الجوال"
                flag = true
            } else {
                if (mobile.text.toString().length != 10) {
                    mobile.error = "الرجاء ادخال رقم جوال صحيح"
                    flag = true
                } else if (mobile.text.toString()[0] != '0') {
                    mobile.error = "الرجاء ادخال رقم جوال صحيح"
                    flag = true
                }
            }
            if (email.text.toString().isEmpty()) {
                email.error = "الرجاء ادخال البريد الالكتروني"
                flag = true
            } else {
                val pattern: Pattern
                val matcher: Matcher

                val EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                        "[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                        "A-Z]{2,7}$"

                //compile = تحقق
                pattern = Pattern.compile(EMAIL_PATTERN)
                matcher = pattern.matcher(email.text.toString())
                if (!matcher.matches()) {

                    email.error = "الرحاء ادخال بريد الكتروني صحيح"
                    flag = true
                }

            }
            if (cpassword.text.toString().isEmpty()) {
                cpassword.error = "الرجاء ادخال تأكيد كلمة المرور"
                flag = true
            }
            if (password.text.toString() != cpassword.text.toString()) {
                cpassword.error = "الرجاء التأكد من تطابق كلمة المرور"
                flag = true
            }

            if (password.text.toString().isEmpty()) {
                password.error = "الرجاء ادخال كلمة المرور"
                flag = true
            } else {
                if (password.text.toString() == password.text.toString().toLowerCase()) {
                    password.error = "يجب ان تحتوي كلمة المرور على حروف كبيرة وصغيرة"
                    flag = true
                }
                if (password.text.toString().length < 8) {
                    password.error = "يجب ان لا تقل كلمة المرور عن 8 حروف"
                    flag = true

                }
            }

            if (!flag) {
                val query = mDatabase!!.child("Users")
                //addListenerForSingleValueEvent = اقدر اخليها تتدخل على كل وحدة بس أنا ابيها تستمع لها بالأول كلها ثم تدخل مره وحده
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    // dataSnapshot = مكونات القاعدة
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (user in dataSnapshot.children) {

                                if(user.child("username").getValue().toString().equals(username.text.toString())) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "اسم المتتخدم مستخدم مسبقا!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    break
                                }else if(user.child("mobile").getValue().toString().equals(mobile.text.toString())) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "رقم الموبايل مستخدم مسبقا!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    break
                                }else if(user.child("email").getValue().toString().equals(email.text.toString())) {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "البريد الالكتروني مستخدم مسبقا!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    break
                                }else{
                                    //UUID.randomUUID = يطلع ID هيكسا ديسمال في قاعدة البيانات
                                    var a = UUID.randomUUID().toString()
                                    uploadImage(a)
                                    //uniqueKey = يبدأ ينشئ مفتاح لكل مستخدم وهذا المفتاح فيه معلومات
                                    val uniqueKey = mDatabase!!.child("Users").push().key

                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("username").setValue(username.text.toString())
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("name").setValue(name.text.toString())
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("email").setValue(email.text.toString())
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("password").setValue(password.text.toString())
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("mobile").setValue(mobile.text.toString())
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("photo").setValue(a)
                                    mDatabase!!.child("Users").child(uniqueKey.toString()).child("type").setValue(type)
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "تم تسجيل الحساب!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val i = Intent(baseContext, MainActivity::class.java)
                                    //addFlags = يبدأ ينظف عشان يمنعني من الرجوع
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    startActivity(i)
                                    finish()
                                    break
                                }
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })


            }

        })




    }
//uploadImage = فيها ثلاث اشياء تم تحميل الصورة واللود ولم يتم تحميل الصورة بس هنا حطينا فقط تم تحميل الصورة
    private fun uploadImage(name:String) {

        if (filePath != null) {

            val ref = storageReference?.child("images/" + name)
            ref!!.putFile(filePath)
                .addOnSuccessListener {
                    Toast.makeText(this@RegisterActivity, "تم تسجيل المستخدم", Toast.LENGTH_SHORT).show()

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
//imageUri = رابط الصورة // imageStream = ?
                val imageUri = data!!.data
                val imageStream: InputStream?
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


// showFileChooser = يأخذ الأذن عشان يسمح لي للوصول للصور
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
