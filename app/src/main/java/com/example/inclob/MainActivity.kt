package com.example.inclob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Initialize Firebase Auth
        auth = Firebase.auth
        Handler(Looper.getMainLooper()).postDelayed({
            checkUserStatus()
        }, 3000)

    }
    private fun checkUserStatus() {
        if (auth.currentUser != null) {
            // Pengguna sudah masuk, arahkan ke HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            // Pengguna belum masuk, arahkan ke LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}