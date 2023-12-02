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


class AlamatFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "             Alamat Domisili"
            // Tambahkan tombol kembali (optional)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val provinsi = resources.getStringArray(R.array.provinsi)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, provinsi)
        val acProvinsi = view.findViewById<AutoCompleteTextView>(R.id.ac_provinsi)
        acProvinsi.setAdapter(arrayAdapter)
        val etKota = view.findViewById<EditText>(R.id.et_kota)
        val etKecamatan = view.findViewById<EditText>(R.id.et_kecamatan)
        val etAlamat = view.findViewById<EditText>(R.id.et_alamat)


        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)

        btnLanjut.setOnClickListener {
            val bundle = arguments
            bundle?.putString("provinsi", acProvinsi.text.toString())
            bundle?.putString("kota", etKota.text.toString())
            bundle?.putString("kecamatan", etKecamatan.text.toString())
            bundle?.putString("alamat", etAlamat.text.toString())


            val fragThree = bundle?.let { it1 -> InfoDisabilitasFragment.newInstance(it1) }
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                if (fragThree != null) {
                    replace(R.id.fragment_main,fragThree, InfoDisabilitasFragment::class.java.simpleName)
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alamat, container, false)
    }

    companion object {
        fun newInstance(bundle: Bundle): AlamatFragment {
            val fragment = AlamatFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}