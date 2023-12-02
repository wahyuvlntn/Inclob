package com.example.inclob

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SelesaiFragment : Fragment() {
    private lateinit var tvSelesai: TextView
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Selesai"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        auth = Firebase.auth
        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val username = arguments?.getString("username")
            val nama = arguments?.getString("nama")
            val noTelp = arguments?.getString("noTelp")
            val email = arguments?.getString("email")
            val password = arguments?.getString("password")
            val tglLahir = arguments?.getString("tglLahir")
            val jenisKelamin = arguments?.getString("jenisKelamin")
            val provinsi = arguments?.getString("provinsi")
            val kota = arguments?.getString("kota")
            val kecamatan = arguments?.getString("kecamatan")
            val alamat = arguments?.getString("alamat")
            val keteranganDisabilitas = arguments?.getString("keteranganDisabilitas")
            val jenisDisabilitas = arguments?.getString("jenisDisabilitas")
            val gelar = arguments?.getString("gelar")
            val thnLulusSekolah = arguments?.getString("thnLulusSekolah")
            val thnMulaiSekolah = arguments?.getString("thnMulaiSekolah")
            val jurusan = arguments?.getString("jurusan")
            val sekolah = arguments?.getString("sekolah")
            val thnMulaiKerja = arguments?.getString("thnMulaiKerja")
            val thnSelesaiKerja = arguments?.getString("thnSelesaiKerja")
            val blnMulaiKerja = arguments?.getString("blnMulaiKerja")
            val blnSelesaiKerja = arguments?.getString("blnSelesaiKerja")
            val perusahaan = arguments?.getString("perusahaan")
            val posisi = arguments?.getString("posisi")





            // Memastikan semua data ada sebelum melanjutkan
            if (username != null && nama != null && noTelp != null && email != null && password != null && tglLahir != null && jenisKelamin!=null) {
                register(username, nama, noTelp, email, password, tglLahir,jenisKelamin,provinsi, kota, kecamatan, alamat,jenisDisabilitas,keteranganDisabilitas, gelar, thnMulaiSekolah, thnLulusSekolah, sekolah,jurusan, perusahaan, posisi, thnMulaiKerja, thnSelesaiKerja, blnMulaiKerja,blnSelesaiKerja)
            }
        }
    }

    private fun register(
        username: String,
        nama: String,
        noTelp: String,
        email: String,
        password: String,
        tglLahir: String,
        jenisKelamin: String,
        provinsi: String?,
        kota: String?,
        kecamatan: String?,
        alamat: String?,
        jenisDisabilitas: String?,
        keteranganDisabilitas: String?,
        gelar: String?,
        thnMulaiSekolah: String?,
        thnLulusSekolah: String?,
        sekolah: String?,
        jurusan: String?,
        perusahaan: String?,
        posisi: String?,
        thnMulaiKerja: String?,
        thnSelesaiKerja: String?,
        blnMulaiKerja: String?,
        blnSelesaiKerja: String?
    ) {
        auth = Firebase.auth
        val db = Firebase.firestore

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid
                    userId?.let {
                        saveUserDataToFirestore(it, username, nama, noTelp, email, tglLahir, jenisKelamin, provinsi, kota, kecamatan, alamat, jenisDisabilitas,keteranganDisabilitas,  gelar, thnMulaiSekolah, thnLulusSekolah, sekolah,jurusan,perusahaan, posisi, thnMulaiKerja, thnSelesaiKerja, blnMulaiKerja,blnSelesaiKerja)
                    }
                    startActivity(Intent(activity, HomeActivity::class.java))
                }
            }
    }

    private fun saveUserDataToFirestore(
        userId: String,
        username: String,
        nama: String,
        noTelp: String,
        email: String,
        tglLahir: String,
        jenisKelamin: String,
        provinsi: String?,
        kota: String?,
        kecamatan: String?,
        alamat: String?,
        jenisDisabilitas: String?,
        keteranganDisabilitas: String?,
        gelar: String?,
        thnMulaiSekolah: String?,
        thnLulusSekolah: String?,
        sekolah: String?,
        jurusan: String?,
        perusahaan: String?,
        posisi: String?,
        thnMulaiKerja: String?,
        thnSelesaiKerja: String?,
        blnMulaiKerja: String?,
        blnSelesaiKerja: String?
    ) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "nama" to nama,
            "username" to username,
            "noTelp" to noTelp,
            "email" to email,
            "tglLahir" to tglLahir,
            "jenisKelamin" to jenisKelamin,
            "provinsi" to provinsi,
            "kota" to kota,
            "kecamatan" to kecamatan,
            "alamat" to alamat,
            "jenisDisabilitas" to jenisDisabilitas,
            "keteranganDisabilitas" to keteranganDisabilitas,
            "gelar" to gelar,
            "thnMulaiSekolah" to thnMulaiSekolah,
            "thnLulusSekolah" to thnLulusSekolah,
            "sekolah" to sekolah,
            "jurusan" to jurusan,
            "perusahaan" to perusahaan,
            "posisi" to posisi,
            "thnMulaiKerja" to thnMulaiKerja,
            "thnSelesaiKerja" to thnSelesaiKerja,
            "blnMulaiKerja" to blnMulaiKerja,
            "blnSelesaiKerja" to blnSelesaiKerja,
            // ... tambahkan field lain sesuai kebutuhan
        )

        db.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("pengecekanbenar", "DocumentSnapshot added with ID: $userId")
            }
            .addOnFailureListener { e ->
                Log.w("pengecekansalah", "Error adding document", e)
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selesai, container, false)
    }


    companion object {
        fun newInstance(bundle: Bundle): SelesaiFragment {
            val fragment = SelesaiFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}