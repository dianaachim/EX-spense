package com.example.ex_spences

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mFirebaseAuth = FirebaseAuth.getInstance()

        register_button.setOnClickListener {
            val email = email_et.text.toString();
            val password = password_et.text.toString()
            val confirmed_password = confirm_password_et.text.toString();
//            val progressDialog = ProgressDialog(this)

//            Log.d("RegisterActivity", "email is " + email)
//            Log.d("RegisterActivity", "password is $password")

            if (TextUtils.isEmpty(email)) {
                throw Exception("Please enter email address")
            }
            if (TextUtils.isEmpty(password)) {
                throw Exception("Please enter password")
            }
            if (password!=confirmed_password){
                throw Exception("Please confirm the password")
            }


            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        mFirebaseAuth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
//                                    register_progress_bar.visibility = View.GONE
                                    AlertDialog.Builder(this)
                                        .setTitle("Success")
                                        .setMessage("Account created successfully! To complete the registration process, please verify your e-mail address.")
                                        .setPositiveButton("Confirm", null)
                                        .show()
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    showMessage(it.exception?.message.toString())
                                }
                            }
                    } else {
//                        register_progress_bar.visibility = View.GONE
                        showMessage(it.exception?.message.toString())
                    }
                }
        }
    }




    private fun showMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    companion object {

        private const val RC_SIGN_IN = 123
    }


}
