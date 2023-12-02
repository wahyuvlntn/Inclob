package com.example.inclob

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.DateFormatSymbols
import java.util.Calendar


class PekerjaanFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val etPosisi = view.findViewById<EditText>(R.id.et_posisi)
        val etPerusahaan = view.findViewById<EditText>(R.id.et_perusahaan)


        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {
            val bundle = arguments
            bundle?.putString("posisi", etPosisi.text.toString())
            bundle?.putString("thnSelesaiKerja", acThnSelesai.text.toString())
            bundle?.putString("thnMulaiKerja", acThnMulai.text.toString())
            bundle?.putString("blnSelesaiKerja", acBlnSelesai.text.toString())
            bundle?.putString("blnMulaiKerja", acBlnMulai.text.toString())
            bundle?.putString("perusahaan", etPerusahaan.text.toString())


            val fragSix = bundle?.let { it1 -> SelesaiFragment.newInstance(it1) }
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                if (fragSix != null) {
                    replace(R.id.fragment_main,fragSix, SelesaiFragment::class.java.simpleName)
                }
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
        fun newInstance(bundle: Bundle): PekerjaanFragment {
            val fragment = PekerjaanFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}