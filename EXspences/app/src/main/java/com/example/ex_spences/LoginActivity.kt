package com.example.ex_spences

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider


class LoginActivity : AppCompatActivity() {

    lateinit var callbackManager: CallbackManager
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var loginButton: LoginButton

    private fun showMessage(message:String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private fun checkUserSession() {
        val currentUser = mFirebaseAuth!!.currentUser
        if (currentUser != null) {

                val mainMenuIntent = Intent(this, MainActivity::class.java)
                startActivity(mainMenuIntent)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FacebookSdk.sdkInitialize(this);

        mFirebaseAuth = FirebaseAuth.getInstance()
        checkUserSession()

        do_not_have_account_tv.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            val email = email_et.text.toString()
            val password = password_et.text.toString()

            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                    override fun onComplete(task: Task<AuthResult?>) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            showMessage("Login success")
                            val user: FirebaseUser? = mFirebaseAuth.getCurrentUser()

                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage("Login failed")
                            updateUI(null)
                        }

                        // ...
                    }
                })
        }



    }



    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }else {
            showMessage("Please sign in to continue!")
        }
    }


}
