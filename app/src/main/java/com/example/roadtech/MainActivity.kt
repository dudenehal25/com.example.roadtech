package com.example.roadtech

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btn_signin.setOnClickListener {
            val phoneno = et_signin_email.text.toString()
            val pass = et_signin_password.text.toString()


            run()


            /* mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                 if (it.isSuccessful) {
                     Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show()
                     val intent = Intent(this, profileActivity::class.java)
                     intent.putExtra("email" , email)
                     startActivity(intent)

                     finish()
                 } else {
                     Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
                 }
             }*/


        }


        btn_signup_go.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))

        }


    }

    fun run() {
        val phoneno = et_signin_email.text.toString()
        val pass = et_signin_password.text.toString()

        val reference = FirebaseDatabase.getInstance().getReference("users")
        val checkUser: Query = reference.orderByChild("phoneno").equalTo(phoneno)

        checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MainActivity, "databse error", Toast.LENGTH_SHORT).show()

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val passwordFromDB =
                        dataSnapshot.child(phoneno).child("password").getValue(
                            String::class.java
                        )

                    if (passwordFromDB.equals(pass)) {

                        Toast.makeText(this@MainActivity, "LOGIN SUCCESS", Toast.LENGTH_SHORT)
                            .show()

                        val nameFromDB =
                            dataSnapshot.child(phoneno).child("name").getValue(
                                String::class.java
                            )
                        val usernameFromDB =
                            dataSnapshot.child(phoneno).child("email").getValue(
                                String::class.java
                            )
                        val phoneNoFromDB =
                            dataSnapshot.child(phoneno).child("phoneno").getValue(
                                String::class.java
                            )
                        val intent =
                            Intent(applicationContext, profileActivity::class.java)

                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", usernameFromDB);
                        intent.putExtra("phoneNo", phoneNoFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent)
                    } else Toast.makeText(this@MainActivity, "WRONG PASSWORD", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this@MainActivity, "user not exist", Toast.LENGTH_SHORT)
                    .show()

            }
        })

    }



}