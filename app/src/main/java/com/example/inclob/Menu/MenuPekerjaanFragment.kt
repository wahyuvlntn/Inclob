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
import com.example.inclob.Adapter.PekerjaanAdapter
import com.example.inclob.R
import com.example.inclob.data.Pekerjaan
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale


class MenuPekerjaanFragment : Fragment() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var auth: FirebaseAuth
    private lateinit var searchView: SearchView
    private lateinit var pekerjaanAdapter: PekerjaanAdapter
    private var mListPekerjaan = ArrayList<Pekerjaan>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pekerjaanAdapter = PekerjaanAdapter(requireContext())

        // Mengambil data pekerjaan dari adapter
        pekerjaanAdapter.setOnDataChangedListener {
            // Setelah data berubah, kita bisa menginisialisasi mList dan melakukan pencarian jika diperlukan
            mListPekerjaan.clear()
            mListPekerjaan.addAll(pekerjaanAdapter.getPekerjaanList())
            Log.d("OnCreate", "mList Size: ${mListPekerjaan.size}")
        }
        pekerjaanAdapter.fetchDataFromFirestore()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_pekerjaan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pekerjaanAdapter

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

            val filteredListPekerjaan = ArrayList<Pekerjaan>()
            for (i in mListPekerjaan) {
                Log.d("mList", "mList: $mListPekerjaan")
                if (i.title?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredListPekerjaan.add(i)
                }
            }

            if (filteredListPekerjaan.isEmpty()) {
                Toast.makeText(requireContext(), "Pencarian tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                pekerjaanAdapter.setPekerjaanList(filteredListPekerjaan)
            }
        }

    }

    companion object {
        fun newInstance(): MenuPekerjaanFragment {
            val fragment = MenuPekerjaanFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}