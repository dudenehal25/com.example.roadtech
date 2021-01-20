package com.example.roadtech

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.IOException


class SignupActivity : AppCompatActivity() {
    var imageUriPath: Uri? = null
    companion object{
        const val GALLERY = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        btn_signup.setOnClickListener {

            val email = et_signup_email.text.toString()
            val pass = et_signup_password.text.toString()
            val name = et_signup_name.text.toString()
            val phone = et_signup_phone.text.toString()

            var rootnode = FirebaseDatabase.getInstance()
            var refrence = rootnode.getReference("users")

            var userHelperClass = UserHelperClass(name, email, phone, pass)

            refrence.child(phone).setValue(userHelperClass)

            if (validateForm(name, email, pass, phone)) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                            finish()

                        } else Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        go_Back.setOnClickListener {
            finish()
        }

        ivSignup.setOnClickListener {
            choosePhotoFromGallery()
        }
    }

    //Ask permisiion and start Acticity for result(FOR GALLERY)
    private fun choosePhotoFromGallery() {
        Dexter.withContext(this).withPermissions(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report!!.areAllPermissionsGranted()) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationalDialogForPermissions()
            }
        }
        ).onSameThread().check()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        // Here this is used to get an bitmap from URI
                        @Suppress("DEPRECATION")
                        val selectedImageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)

                      //  imageUriPath = saveImagetoInternalStorage(selectedImageBitmap)
                        Log.e("SAVED", imageUriPath.toString())

                        ivSignup!!.setImageBitmap(selectedImageBitmap) // Set the selected image from GALLERY to imageView.
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    }


    private fun validateForm(name: String, email: String, Password: String, Phoneno: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                Toast.makeText(this, "name is empty", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "email is empty", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(Phoneno) -> {
                Toast.makeText(this, "phone is empty", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(Password) -> {
                Toast.makeText(this, "pass is empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }


}