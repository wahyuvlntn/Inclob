package com.example.inclob


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvRegister: TextView
    lateinit var googleSignInClient: GoogleSignInClient
    companion object {
        private const val RC_SIGN_IN = 123
    }

    override fun onStart() {
        super.onStart()
        // Check if the user is already authenticated
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Redirect to HomeActivity if the user is authenticated
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.black))

        auth = Firebase.auth

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        tvRegister = findViewById(R.id.tv_register)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)


        val btnLogin = findViewById<Button>(R.id.btn_login)
        btnLogin.setOnClickListener {
            val email  = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            login(email, password)

        }

        val ibtnGoogle = findViewById<ImageButton>(R.id.ibtn_google)
        ibtnGoogle.setOnClickListener {
            googleSignIn()
        }


        tvRegister.setOnClickListener {
            startActivity(Intent(this, PendaftaranActivity::class.java))

        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In berhasil, lanjutkan dengan Firebase Authentication
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                // Google Sign-In gagal
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in dengan Google berhasil, lanjutkan ke aktivitas selanjutnya
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    // Sign in dengan Google gagal
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun googleSignIn() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is already authenticated, you can show a message or take any appropriate action
            Toast.makeText(
                baseContext,
                "You are already authenticated with an account.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // User is not authenticated, proceed with Google Sign-In
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }
    }

}