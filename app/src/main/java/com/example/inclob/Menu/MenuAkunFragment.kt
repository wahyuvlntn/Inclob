package com.example.inclob.Menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.inclob.LoginActivity
import com.example.inclob.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MenuAkunFragment : Fragment() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var auth: FirebaseAuth
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvTglLahir: TextView
    private lateinit var tvJenisKelamin: TextView
    private lateinit var tvAlamat: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_akun, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        tvNama = view.findViewById(R.id.tv_nama)
        tvEmail = view.findViewById(R.id.tv_email)
        tvUsername = view.findViewById(R.id.tv_username)
        tvTglLahir = view.findViewById(R.id.tv_tglLahir)
        tvJenisKelamin = view.findViewById(R.id.tv_jenisKelamin)
        tvAlamat = view.findViewById(R.id.tv_alamat)
        // Add other TextViews as needed

        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Fetch user data from Firestore based on UID
            fetchUserData(currentUser.uid)
        } else {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }

        val btnLogout = view.findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun fetchUserData(uid: String) {
        val firestore = FirebaseFirestore.getInstance()
        val userDocument = firestore.collection("users").document(uid)

        userDocument.get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Populate TextViews with user data
                    tvUsername.text = documentSnapshot.getString("username")
                    tvNama.text = documentSnapshot.getString("nama")
                    tvEmail.text = documentSnapshot.getString("email")
                    tvTglLahir.text = documentSnapshot.getString("tglLahir")
                    tvAlamat.text = documentSnapshot.getString("alamat")
                    tvJenisKelamin.text = documentSnapshot.getString("jenisKelamin")

                    // Add other fields as needed
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
                // You may want to show an error message or log the exception
            }
    }
}
