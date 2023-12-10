package com.example.inclob.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.inclob.R
import com.example.inclob.pekerjaanfrag.DetailPekerjaanFragment
import com.example.inclob.pekerjaanfrag.HasilSeleksiFragment
import com.example.inclob.pekerjaanfrag.PeninjauanBerkasFragment
import com.example.inclob.pekerjaanfrag.PeninjauanWawancaraFragment
import com.example.inclob.pekerjaanfrag.WawancaraFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PekerjaanActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pekerjaan)
        getWindow().setStatusBarColor(getResources().getColor(R.color.black))
        auth = FirebaseAuth.getInstance()
        val docId = intent.getStringExtra("doc_id")
        val user = auth.currentUser
        val db = Firebase.firestore
        user?.let {
            val userDocument = db.collection("users").document(user.uid)
            userDocument.collection("melamar")
                .whereEqualTo("idPekerjaan", docId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        Log.d("PekerjaanAdapter", "No documents found in querySnapshot")
                        val fragOne = DetailPekerjaanFragment.newInstance(docId)
                        supportFragmentManager.beginTransaction().apply {
                            //fragment melamar
                            replace(R.id.content, fragOne)
                            commit()
                        }
                    }
                    for (document in querySnapshot.documents) {
                        // Ambil nilai status1 dari setiap dokumen
                        val status1Value = document.getString("status1")
                        val status2Value = document.getString("status2")
                        Log.d("PekerjaanAdapter", "Nilai dari status1: $status1Value")

                        if ("pending".equals(status1Value, ignoreCase = true)) {
                            Log.d("PekerjaanAdapter", "Condition met - status1 is 'pending'")
                            val peninjauanBerkasFragment = PeninjauanBerkasFragment.newInstance(docId)
                            supportFragmentManager.beginTransaction().apply {
                                //fragment melamar
                                replace(R.id.content, peninjauanBerkasFragment)
                                commit()
                            }

                            // Lakukan sesuatu dengan nilai status1Value di sini
                        }
                        else if("passed".equals(status1Value, ignoreCase = true)){
                            if("none".equals(status2Value, ignoreCase = true)){
                                Log.d("PekerjaanAdapter", "Condition met - status1 is 'passed'")
                                val wawancaraFragment = WawancaraFragment.newInstance(docId)
                                supportFragmentManager.beginTransaction().apply {
                                    //fragment melamar
                                    replace(R.id.content, wawancaraFragment)
                                    commit()
                                }
                            }
                            else if ("pending".equals(status2Value, ignoreCase = true)){
                                val peninjauanWawancaraFragment = PeninjauanWawancaraFragment.newInstance(docId)
                                supportFragmentManager.beginTransaction().apply {
                                    //fragment melamar
                                    replace(R.id.content, peninjauanWawancaraFragment)
                                    commit()
                                }
                            }
                            else if("passed".equals(status2Value, ignoreCase = true)){
                                val hasilSeleksiFragment = HasilSeleksiFragment.newInstance(docId)
                                supportFragmentManager.beginTransaction().apply {
                                    //fragment melamar
                                    replace(R.id.content, hasilSeleksiFragment)
                                    commit()
                                }
                            }

                        }
                    }


                }
                .addOnFailureListener { e ->
                    Log.e("PekerjaanAdapter", "Gagal mendapatkan data melamar: $e")
                }
        }



    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}