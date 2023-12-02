package com.example.inclob.pekerjaanfrag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.inclob.R
import com.example.inclob.data.Pekerjaan
import com.google.firebase.firestore.FirebaseFirestore

class WawancaraFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_wawancara, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        documentId?.let { fetchPekerjaanDetails(it) }
    }

    companion object {
        private const val TAG = "WawancaraFragment"
        // Function to create a new instance of the fragment with the document ID as an argument
        fun newInstance(documentId: String?): WawancaraFragment {
            val fragment = WawancaraFragment()
            val args = Bundle()
            args.putString("docId", documentId)
            fragment.arguments = args
            return fragment
        }
    }
    private fun fetchPekerjaanDetails(documentId: String) {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("pekerjaan")
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

                // Map data dokumen ke model (gantilah Pekerjaan::class.java dengan kelas model yang sesuai)
                val pekerjaan = document.toObject(Pekerjaan::class.java)

                // Update TextView elements with data from the Pekerjaan model
                view?.findViewById<TextView>(R.id.tv_title)?.text = pekerjaan?.title
                val image = pekerjaan?.gambar

                image.let {
                    // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
                    view?.findViewById<ImageView>(R.id.iv_pekerjaan)?.let { it1 ->
                        Glide.with(this)
                            .load(it)
                            .into(it1)
                    }
                }
                view?.findViewById<TextView>(R.id.tv_perusahaan)?.text = pekerjaan?.perusahaan
                view?.findViewById<TextView>(R.id.tv_kota)?.text = pekerjaan?.kota

            }
            .addOnFailureListener { exception ->
                // Handle error
                // You might want to show an error message or log the exception
            }
    }
}