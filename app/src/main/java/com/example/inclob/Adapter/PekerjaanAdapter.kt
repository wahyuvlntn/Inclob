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

class PekerjaanAdapter(val context: Context) :
    RecyclerView.Adapter<PekerjaanAdapter.PekerjaanViewHolder>() {

    private val pekerjaanList: MutableList<Pekerjaan> = mutableListOf()
    private var onDataChangedListener: (() -> Unit)? = null

    fun setOnDataChangedListener(listener: () -> Unit) {
        onDataChangedListener = listener
    }

    inner class PekerjaanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.tv_title)
        var textViewPerusahaan: TextView = itemView.findViewById(R.id.tv_perusahaan)
        var textViewKota: TextView = itemView.findViewById(R.id.tv_kota)
        var iconView: ImageView = itemView.findViewById(R.id.iv_pekerjaan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PekerjaanViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pekerjaan, parent, false)
        return PekerjaanViewHolder(view)
    }

    fun getPekerjaanList(): List<Pekerjaan> {
        return pekerjaanList
    }

    override fun onBindViewHolder(holder: PekerjaanViewHolder, position: Int) {
        val pekerjaan = pekerjaanList[position]

        holder.textViewTitle.text = pekerjaan.title
        holder.textViewPerusahaan.text = pekerjaan.perusahaan
        holder.textViewKota.text = pekerjaan.kota

        val image = pekerjaan.gambar
        image.let {
            // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
            Glide.with(context)
                .load(it)
                .into(holder.iconView)
        }


        holder.itemView.setOnClickListener {
            Log.d("PekerjaanAdapter", "Item Clicked, ID: ${pekerjaan.id}")
            val intent = Intent(context, PekerjaanActivity::class.java)
            intent.putExtra("doc_id",pekerjaan.id)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return pekerjaanList.size
    }

    // Fungsi ini digunakan untuk mengatur data pekerjaan ke dalam adapter
    fun setPekerjaanList(newList: List<Pekerjaan>) {
        pekerjaanList.clear()
        pekerjaanList.addAll(newList)
        notifyDataSetChanged()
    }

    // Mengambil data pekerjaan dari Firestore
    fun fetchDataFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val pekerjaanListFromFirestore = mutableListOf<Pekerjaan>()

        firestore.collection("pekerjaan")
            .get()
            .addOnSuccessListener { result ->
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
                    val pekerjaan = Pekerjaan(id,gambarUrl, title, perusahaan, kota)
                    pekerjaanListFromFirestore.add(pekerjaan)

                }
                // Memperbarui adapter setelah mendapatkan data
                setPekerjaanList(pekerjaanListFromFirestore)
                onDataChangedListener?.invoke()
            }
            .addOnFailureListener { exception ->
                // Handle error
                Toast.makeText(context, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



}

