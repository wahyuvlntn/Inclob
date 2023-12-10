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
import com.example.inclob.Adapter.PelatihanAdapter
import com.example.inclob.R
import com.example.inclob.data.Pekerjaan
import com.example.inclob.data.Pelatihan
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.Locale

class MenuBerandaFragment : Fragment() {
    private lateinit var topAppBar: MaterialToolbar
    private lateinit var auth: FirebaseAuth
    private lateinit var pekerjaanAdapter: PekerjaanAdapter
    private lateinit var pelatihanAdapter: PelatihanAdapter
    private lateinit var searchView: SearchView
    private var mListPekerjaan = ArrayList<Pekerjaan>()
    private var mListPelatihan = ArrayList<Pelatihan>()

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
        return inflater.inflate(R.layout.fragment_menu_beranda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        // Set up RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = pekerjaanAdapter
        val recyclerViewPelatihan: RecyclerView = view.findViewById(R.id.rv_pelatihan)
        recyclerViewPelatihan.setHasFixedSize(true)
        recyclerViewPelatihan.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
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

            val filteredListPekerjaan = ArrayList<Pekerjaan>()
            for (i in mListPekerjaan) {
                Log.d("mList", "mList: $mListPekerjaan")
                if (i.title?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredListPekerjaan.add(i)
                }
            }
            val filteredListPelatihan = ArrayList<Pelatihan>()
            for (i in mListPelatihan) {
                Log.d("mList", "mList: $mListPelatihan")
                if (i.title?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredListPelatihan.add(i)
                }
            }

            if (filteredListPelatihan.isEmpty() and filteredListPekerjaan.isEmpty()) {
                Toast.makeText(requireContext(), "Pencarian tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                pekerjaanAdapter.setPekerjaanList(filteredListPekerjaan)
                pelatihanAdapter.setPelatihanList(filteredListPelatihan)
            }
        }

    }


    companion object {
        fun newInstance(): MenuBerandaFragment {
            val fragment = MenuBerandaFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}
