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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AlamatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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


        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)

        btnLanjut.setOnClickListener {
            val fragThree = InfoDisabilitasFragment()
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                replace(R.id.fragment_main,fragThree, InfoDisabilitasFragment::class.java.simpleName)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlamatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlamatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}