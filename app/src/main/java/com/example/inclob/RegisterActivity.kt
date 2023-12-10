package com.example.inclob

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var etNama: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var btnRegister: Button
    lateinit var progressDialog : ProgressDialog
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().setStatusBarColor(getResources().getColor(R.color.black))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Informasi Pribadi"
        }

        auth = Firebase.auth
        etNama = findViewById(R.id.et_nama)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Logging")
        progressDialog.setMessage("Silahkan tunggu")
        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)
        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btnRegister.setOnClickListener {
            if(etNama.text.isNotEmpty()&&etEmail.text.isNotEmpty()&&etPassword.text.isNotEmpty()){
                register()
            }else{
                Toast.makeText(this, "Silahkan isi semua data", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu,menu)
        return true
    }



    private fun register() {
        val nama = etNama.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val userUpdateProfile = userProfileChangeRequest {
                        displayName = nama
                    }
                    val user = task.result.user
                    user!!.updateProfile(userUpdateProfile)
                        .addOnCompleteListener {
                            progressDialog.dismiss()
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                }
                else{
                    progressDialog.dismiss()
                }
            }
            .addOnFailureListener { error2 ->
                Toast.makeText(this, error2.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}