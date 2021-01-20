package com.example.roadtech

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile.*

class profileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        showdata()






    }
    fun showdata(){
        val intent = intent
        val user_username = intent.getStringExtra("email")
        val user_name = intent.getStringExtra("name")
        val user_phoneNo = intent.getStringExtra("phoneNo")
        val user_password = intent.getStringExtra("password")

        name.text = user_name
        email.text = user_username
        phone.text = user_phoneNo
        pass.text = user_password
    }
}