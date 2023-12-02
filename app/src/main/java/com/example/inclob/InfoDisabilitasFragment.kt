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


class InfoDisabilitasFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Informasi Disabilitas"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)

        val jenisDisabilitasData = resources.getStringArray(R.array.jenis_disabilitas)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,jenisDisabilitasData)
        val acJenisDisabilitas = view.findViewById<AutoCompleteTextView>(R.id.ac_jenis_disabilitas)
        acJenisDisabilitas.setAdapter(arrayAdapter)
        val etKeterangan = view.findViewById<EditText>(R.id.et_keterangan)


        btnLanjut.setOnClickListener {
            val bundle = arguments
            bundle?.putString("jenisDisabilitas", acJenisDisabilitas.text.toString())
            bundle?.putString("keteranganDisabilitas", etKeterangan.text.toString())

            val fragFour = bundle?.let { it1 -> PendidikanFragment.newInstance(it1) }
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                if (fragFour != null) {
                    replace(R.id.fragment_main,fragFour, PendidikanFragment::class.java.simpleName)
                }
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_info_disabilitas, container, false)
    }

    companion object {
        fun newInstance(bundle: Bundle): InfoDisabilitasFragment {
            val fragment = InfoDisabilitasFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}