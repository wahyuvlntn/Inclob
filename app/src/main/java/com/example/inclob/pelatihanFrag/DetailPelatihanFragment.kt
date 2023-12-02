package com.example.inclob.pelatihanFrag

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inclob.R
import com.example.inclob.data.Pelatihan
import com.google.firebase.firestore.FirebaseFirestore


class DetailPelatihanFragment : Fragment() {
    private var documentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentId = arguments?.getString("docId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_pelatihan, container, false)
    }

    companion object {
        // Function to create a new instance of the fragment with the document ID as an argument
        fun newInstance(documentId: String?): DetailPelatihanFragment {
            val fragment = DetailPelatihanFragment()
            val args = Bundle()
            args.putString("docId", documentId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Detail Pelatihan"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // Fetch data from Firestore using the document ID
        documentId?.let { fetchPelatihanDetails(it) }

        val btnIkuti = view.findViewById<Button>(R.id.btn_ikuti)
        btnIkuti.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.atmago.com/acara/pelatihan-bahasa-inggris-dasar-16-22-november-2021_d5877775-022f-4c03-a1ce-55042965aec6"))
            startActivity(intent)
        }
    }

    private fun fetchPelatihanDetails(documentId: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("pelatihan")
            .whereEqualTo("id",documentId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Handle case when document doesn't exist
                    // Misalnya, tampilkan pesan bahwa dokumen tidak ditemukan
                    Log.d("FirestoreData", "Dokumen tidak ditemukan")
                    return@addOnSuccessListener
                }

                // Hanya mengambil satu dokumen karena seharusnya ID dokumen unik
                val document = documents.documents[0]


                val pelatihan = document.toObject(Pelatihan::class.java)

                view?.findViewById<TextView>(R.id.tv_title)?.text = pelatihan?.title
                val image = pelatihan?.gambar

                image.let {
                    // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
                    view?.findViewById<ImageView>(R.id.iv_pelatihan)?.let { it1 ->
                        Glide.with(this)
                            .load(it)
                            .into(it1)
                    }
                }

                view?.findViewById<TextView>(R.id.tv_desk_pelatihan)?.text = pelatihan?.deskripsi
            }
            .addOnFailureListener { exception ->
                // Handle error
                // You might want to show an error message or log the exception
            }
    }

}