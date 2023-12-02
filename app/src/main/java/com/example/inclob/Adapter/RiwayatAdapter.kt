package com.example.inclob.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inclob.R
import com.example.inclob.activity.PekerjaanActivity
import com.example.inclob.data.Pekerjaan
import com.google.firebase.firestore.FirebaseFirestore

class RiwayatAdapter(val context: Context) :
    RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    private val riwayatList: MutableList<Pekerjaan> = mutableListOf()
    private var onDataChangedListener: (() -> Unit)? = null

    fun setOnDataChangedListener(listener: () -> Unit) {
        onDataChangedListener = listener
    }

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.tv_title)
        var textViewPerusahaan: TextView = itemView.findViewById(R.id.tv_perusahaan)
        var textViewKota: TextView = itemView.findViewById(R.id.tv_kota)
        var iconView: ImageView = itemView.findViewById(R.id.iv_pekerjaan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pekerjaan, parent, false)
        return RiwayatViewHolder(view)
    }

    fun getRiwayatList(): List<Pekerjaan> {
        return riwayatList
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val pekerjaan = riwayatList[position]

        holder.textViewTitle.text = pekerjaan.title
        holder.textViewPerusahaan.text = pekerjaan.perusahaan
        holder.textViewKota.text = pekerjaan.kota

        val image = pekerjaan.gambar
        image.let {
            // Using Glide to load the image from the URL into ImageView
            Glide.with(context)
                .load(it)
                .into(holder.iconView)
        }

        holder.itemView.setOnClickListener {
            Log.d("RiwayatAdapter", "Item Clicked, ID: ${pekerjaan.id}")
            val intent = Intent(context, PekerjaanActivity::class.java)
            intent.putExtra("doc_id", pekerjaan.id)
            startActivity(context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return riwayatList.size
    }

    // Function to set the list of pekerjaan in the adapter
    fun setRiwayatList(newList: List<Pekerjaan>) {
        riwayatList.clear()
        riwayatList.addAll(newList)
        notifyDataSetChanged()
    }

    // Fetching data from Firestore based on "idPekerjaan" field in "melamar" collection
    fun fetchDataFromFirestore(userId: String) {
        val firestore = FirebaseFirestore.getInstance()
        val riwayatListFromFirestore = mutableListOf<Pekerjaan>()

        firestore.collection("users").document(userId)
            .collection("melamar")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val idPekerjaan = document.getString("idPekerjaan")
                    if (idPekerjaan != null) {
                        firestore.collection("pekerjaan")
                            .whereEqualTo("id",idPekerjaan)
                            .get()
                            .addOnSuccessListener {result ->
                                for (document in result) {
                                    Log.d("FirestoreData", "Title: ${document.getString("title")}")
                                    Log.d("FirestoreData", "Gambar: ${document.data}")
                                    Log.d("FirestoreData", "Gambar: ${document.id}")
                                    val id = document.getString("id")
                                    val title = document.getString("title")
                                    val perusahaan = document.getString("perusahaan")
                                    val kota = document.getString("kota")
                                    // Mengakses URL dari referensi gambar

                                    val gambarUrl = document.getString("gambar")
                                    Log.d("FirestoreData", "Gambar URL: $gambarUrl")

                                    // Membuat objek Pekerjaan dan menambahkannya ke list
                                    val riwayatPekerjaan = Pekerjaan(id, gambarUrl, title, perusahaan, kota)
                                    riwayatListFromFirestore.add(riwayatPekerjaan)

                                    setRiwayatList(riwayatListFromFirestore)
                                    onDataChangedListener?.invoke()
                                }
                                setRiwayatList(riwayatListFromFirestore)
                                onDataChangedListener?.invoke()
                            }
//                        firestore.collection("pekerjaan").document(idPekerjaan)
//                            .get()
//                            .addOnSuccessListener { pekerjaanDocument ->
//                                Log.d("FirestoreData", "Title: ${document.getString("title")}")
//                                Log.d("FirestoreData", "Gambar: ${document.data}")
//                                val id = pekerjaanDocument.getString("id")
//                                val title = pekerjaanDocument.getString("title")
//                                val perusahaan = pekerjaanDocument.getString("perusahaan")
//                                val kota = pekerjaanDocument.getString("kota")
//                                val gambarUrl = pekerjaanDocument.getString("gambar")
//                                Log.d("FirestoreData", "Gambar URL: $gambarUrl")
//
//                                val riwayatPekerjaan = Pekerjaan(id, gambarUrl, title, perusahaan, kota)
//                                riwayatListFromFirestore.add(riwayatPekerjaan)
//
//                                // Update adapter after getting data
//                                setRiwayatList(riwayatListFromFirestore)
//                                onDataChangedListener?.invoke()
//                            }
//                            .addOnFailureListener { exception ->
//                                // Handle error
//                                Log.e("RiwayatAdapter", "Error fetching pekerjaan data: ${exception.message}")
//                                Toast.makeText(context, "Error fetching pekerjaan data: ${exception.message}", Toast.LENGTH_SHORT).show()
//                            }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
                Log.e("RiwayatAdapter", "Error fetching melamar data: ${exception.message}")
                Toast.makeText(context, "Error fetching melamar data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
