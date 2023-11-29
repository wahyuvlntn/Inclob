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
import java.text.DateFormatSymbols
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PekerjaanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PekerjaanFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pekerjaan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Pekerjaan Terakhir"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val thnMulai = generateYear(mulai = true)
        val blnMulai = generateMonth()
        val thnSelesai = generateYear(mulai = false)
        val blnSelesai = generateMonth()
        val thnMulaiAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,thnMulai)
        val acThnMulai = view.findViewById<AutoCompleteTextView>(R.id.ac_thn_mulai)
        acThnMulai.setAdapter(thnMulaiAdapter)
        val blnMulaiAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,blnMulai)
        val acBlnMulai = view.findViewById<AutoCompleteTextView>(R.id.ac_bln_mulai)
        acBlnMulai.setAdapter(blnMulaiAdapter)
        val thnSelesaiAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,thnSelesai)
        val acThnSelesai = view.findViewById<AutoCompleteTextView>(R.id.ac_thn_selesai)
        acThnSelesai.setAdapter(thnSelesaiAdapter)
        val blnSelesaiAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,blnSelesai)
        val acBlnSelesai = view.findViewById<AutoCompleteTextView>(R.id.ac_bln_selesai)
        acBlnSelesai.setAdapter(blnSelesaiAdapter)



        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val fragSix = SelesaiFragment()
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                replace(R.id.fragment_main,fragSix, SelesaiFragment::class.java.simpleName)
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

    private fun generateMonth(): Array<String> {
        return DateFormatSymbols().months
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PekerjaanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PekerjaanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}