package com.example.inclob

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class InfoPribadiFragment : Fragment(R.layout.fragment_info_pribadi) {
    private lateinit var etTanggalLahir: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan referensi ke Spinner
        val jenisKelamin = resources.getStringArray(R.array.jenis_kelamin)
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,jenisKelamin)
        val acJenisKelamin= view.findViewById<AutoCompleteTextView>(R.id.ac_jenis_kelamin)
        acJenisKelamin.setAdapter(arrayAdapter)

        // Inisialisasi DatePicker untuk Tanggal Lahir
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Mendapatkan referensi ke EditText Tanggal Lahir
        etTanggalLahir = view.findViewById(R.id.et_tglLahir)

        etTanggalLahir.setOnClickListener {
            showDatePickerDialog()
        }

        val etUsername = view.findViewById<EditText>(R.id.et_username)
        val etNama = view.findViewById<EditText>(R.id.et_nama_lengkap)
        val etNoTelp = view.findViewById<EditText>(R.id.et_notelp)
        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val etPassword = view.findViewById<EditText>(R.id.et_password)
        val etConfirmPassword = view.findViewById<EditText>(R.id.et_confirm_password)

        val btnLanjut = view.findViewById<Button>(R.id.btn_lanjut)
        btnLanjut.setOnClickListener {

            //viewModel.nama = User(etNama.text.toString().trim())
            val bundle = Bundle().apply {
                putString("username", etUsername.text.toString())
                putString("nama", etNama.text.toString())
                putString("noTelp", etNoTelp.text.toString())
                putString("email", etEmail.text.toString())
                putString("password", etPassword.text.toString())
                putString("tglLahir", etTanggalLahir.text.toString())
                putString("jenisKelamin",acJenisKelamin.text.toString())
            }
            val fragFive = SelesaiFragment.newInstance(bundle)

            val fragTwo = AlamatFragment.newInstance(bundle)
            val fragManager = fragmentManager

            fragManager?.beginTransaction()?.apply {
                replace(R.id.fragment_main,fragTwo, AlamatFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Format tanggal yang dipilih (misal: 01 Januari 2023)
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Tampilkan tanggal yang dipilih di TextInputEditText
                etTanggalLahir.setText(formattedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Tampilkan DatePickerDialog
        datePickerDialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_pribadi, container, false)
    }

    companion object {
        fun newInstance(bundle: Bundle): InfoPribadiFragment {
            val fragment = InfoPribadiFragment()
            fragment.arguments = bundle
            return fragment
        }
    }



}