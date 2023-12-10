package com.example.inclob.Menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inclob.Adapter.PelatihanAdapter
import com.example.inclob.R
import com.example.inclob.data.Pelatihan
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale


class MenuPelatihanFragment : Fragment() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var auth: FirebaseAuth
    private lateinit var searchView: SearchView
    private lateinit var pelatihanAdapter: PelatihanAdapter
    private var mListPelatihan = ArrayList<Pelatihan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pelatihanAdapter = PelatihanAdapter(requireContext())
        pelatihanAdapter.setOnDataChangedListener {
            // Setelah data berubah, kita bisa menginisialisasi mList dan melakukan pencarian jika diperlukan
            mListPelatihan.clear()
            mListPelatihan.addAll(pelatihanAdapter.getPelatihanList())
            Log.d("OnCreate", "mList Size: ${mListPelatihan.size}")
        }
        pelatihanAdapter.fetchDataFromFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_pelatihan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        val recyclerViewPelatihan: RecyclerView = view.findViewById(R.id.rv_pelatihan)
        recyclerViewPelatihan.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewPelatihan.adapter = pelatihanAdapter

        val materialCardView = view.findViewById<MaterialCardView>(R.id.ly_search)
        searchView = materialCardView.findViewById(R.id.sv_home)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }
    private fun filterList(query: String?) {
        if (query != null) {

            val filteredListPelatihan = ArrayList<Pelatihan>()
            for (i in mListPelatihan) {
                Log.d("mList", "mList: $mListPelatihan")
                if (i.title?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredListPelatihan.add(i)
                }
            }

            if (filteredListPelatihan.isEmpty()) {
                Toast.makeText(requireContext(), "Pencarian tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                pelatihanAdapter.setPelatihanList(filteredListPelatihan)
            }
        }

    }

}