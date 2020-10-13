package com.example.ex_spences

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    lateinit var mFirebaseAuth: FirebaseAuth

    private fun checkUserSession() {
        val currentUser = mFirebaseAuth!!.currentUser
        if (currentUser != null) {

            val mainMenuIntent = Intent(this, MainActivity::class.java)
            startActivity(mainMenuIntent)

        } else {
            val mainMenuIntent = Intent(this, LoginActivity::class.java)
            startActivity(mainMenuIntent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline() {
        val connection = NetworkUtils.getConnectivityStatus(this)
        if (connection != NetworkUtils.TYPE_NO_CONNECTION) {

            checkUserSession()
        } else {
            android.app.AlertDialog.Builder(this)
                .setTitle("Sorry")
                .setMessage("There is no internet connection! Verify network connection and try again")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        mFirebaseAuth = FirebaseAuth.getInstance()
        isOnline()
    }
}