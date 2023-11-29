package com.example.inclob.Adapter
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inclob.R
import com.example.inclob.data.Pelatihan
import com.google.firebase.firestore.FirebaseFirestore

class PelatihanAdapter(val context: Context) :
    RecyclerView.Adapter<PelatihanAdapter.PelatihanViewHolder>() {

    private val pelatihanList: MutableList<Pelatihan> = mutableListOf()
    private var onDataChangedListener: (() -> Unit)? = null

    fun setOnDataChangedListener(listener: () -> Unit) {
        onDataChangedListener = listener
    }

    inner class PelatihanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.tv_title)
        var iconView: ImageView = itemView.findViewById(R.id.iv_pelatihan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PelatihanViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pelatihan, parent, false)
        return PelatihanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PelatihanViewHolder, position: Int) {
        val pelatihan = pelatihanList[position]

        holder.textViewTitle.text = pelatihan.title

        val image = pelatihan.gambar
        image.let {
            // Menggunakan Glide untuk memuat gambar dari URL ke ImageView
            Glide.with(context)
                .load(it)
                .into(holder.iconView)
        }


        holder.itemView.setOnClickListener {
            // Tambahkan logika klik di sini jika diperlukan
        }
    }

    override fun getItemCount(): Int {
        return pelatihanList.size
    }
    fun getPelatihanList(): List<Pelatihan> {
        return pelatihanList
    }

    // Fungsi ini digunakan untuk mengatur data pelatihan ke dalam adapter
    fun setPelatihanList(newList: List<Pelatihan>) {
        pelatihanList.clear()
        pelatihanList.addAll(newList)
        notifyDataSetChanged()
    }



    // Mengambil data pelatihan dari Firestore
    fun fetchDataFromFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val pelatihanListFromFirestore = mutableListOf<Pelatihan>()

        firestore.collection("pelatihan")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("FirestoreData", "Title: ${document.getString("title")}")
                    Log.d("FirestoreData", "Gambar: ${document.data}")
                    val title = document.getString("title")
                    // Mengakses URL dari referensi gambar

                    val gambarUrl = document.getString("gambar")
                    Log.d("FirestoreData", "Gambar URL: $gambarUrl")

                    // Membuat objek Pelatihan dan menambahkannya ke list
                    val pelatihan = Pelatihan(gambarUrl, title)
                    pelatihanListFromFirestore.add(pelatihan)
                }
                // Memperbarui adapter setelah mendapatkan data
                setPelatihanList(pelatihanListFromFirestore)
                onDataChangedListener?.invoke()
            }
            .addOnFailureListener { exception ->
                // Handle error
                Toast.makeText(context, "Error fetching data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }



}

