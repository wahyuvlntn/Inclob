package com.example.inclob

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    private lateinit var tvNama: TextView
    lateinit var tvEmail: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = Firebase.auth
        tvNama = findViewById(R.id.tv_nama)
        tvEmail = findViewById(R.id.tv_email)
        val currentUser = auth.currentUser
        if (currentUser != null) {
            tvNama.text = currentUser.displayName
            tvEmail.text = currentUser.email
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
        }

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}