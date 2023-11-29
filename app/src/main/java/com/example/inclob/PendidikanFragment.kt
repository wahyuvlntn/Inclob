package com.example.inclob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PendidikanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PendidikanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Pendidikan Terakhir"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        val calendar = Calendar.getInstance()
        val thnMulai = generateYear(mulai = true)
        val thnLulus = generateYear(mulai=false)
        val thnMulaiAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,thnMulai)
        val acThnMulai = view.findViewById<AutoCompleteTextView>(R.id.ac_thn_mulai)
        acThnMulai.setAdapter(thnMulaiAdapter)
        val thnLulusAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,thnLulus)
        val acThnLulus = view.findViewById<AutoCompleteTextView>(R.id.ac_thn_lulus)
        acThnLulus.setAdapter(thnLulusAdapter)

        val gelar = resources.getStringArray(R.array.gelar)
        val gelarAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,gelar)
        val acGelar = view.findViewById<AutoCompleteTextView>(R.id.ac_gelar)
        acGelar.setAdapter(gelarAdapter)


        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val fragFive = PekerjaanFragment()
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                replace(R.id.fragment_main,fragFive, PekerjaanFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun generateYear(mulai: Boolean): Array<String> {
        val startYear = 1990 // Atur tahun awal sesuai kebutuhan
        var endYear = Calendar.getInstance().get(Calendar.YEAR) + 5
        if(mulai){
            endYear = Calendar.getInstance().get(Calendar.YEAR)
        }
        val years = mutableListOf<String>()

        for (i in startYear..endYear) {
            years.add(i.toString())
        }

        return years.toTypedArray()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendidikan, container, false)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PendidikanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PendidikanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}