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
import java.util.Calendar

class PendidikanFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        val etSekolah = view.findViewById<EditText>(R.id.et_sekolah)
        val etJurusan = view.findViewById<EditText>(R.id.et_jurusan)


        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {

            val bundle = arguments
            bundle?.putString("gelar", acGelar.text.toString())
            bundle?.putString("thnLulusSekolah", acThnLulus.text.toString())
            bundle?.putString("thnMulaiSekolah", acThnMulai.text.toString())
            bundle?.putString("jurusan", etSekolah.text.toString())
            bundle?.putString("sekolah", etJurusan.text.toString())



            val fragFive = bundle?.let { it1 -> PekerjaanFragment.newInstance(it1) }
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                if (fragFive != null) {
                    replace(R.id.fragment_main,fragFive, PekerjaanFragment::class.java.simpleName)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pendidikan, container, false)
    }

    companion object {
        fun newInstance(bundle: Bundle): PendidikanFragment {
            val fragment = PendidikanFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}