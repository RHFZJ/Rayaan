package com.example.rayaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ChooseUserActivity : AppCompatActivity() {


    lateinit var parent: Button
    lateinit var daycare: Button
    lateinit var specialist: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_user)

        parent = findViewById(R.id.parent)
        daycare = findViewById(R.id.daycare)
        specialist = findViewById(R.id.specialist)

        parent.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext, RegisterActivity::class.java)
            i.putExtra("type","parent")
            startActivity(i)
        })


        daycare.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext, RegisterActivity::class.java)
            i.putExtra("type","daycare")
            startActivity(i)
        })

        specialist.setOnClickListener(View.OnClickListener {
            val i = Intent(baseContext, RegisterActivity::class.java)
            i.putExtra("type","specialist")
            startActivity(i)
        })
    }
}
