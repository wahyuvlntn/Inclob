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
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoPribadiFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoPribadiFragment : Fragment(R.layout.fragment_info_pribadi) {
    private lateinit var etTanggalLahir: EditText
    private lateinit var viewModel: PendaftaranViewModel

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

            viewModel.saveUserData(
                nama = etNama.text.toString().trim(),
                username = etUsername.text.toString().trim(),
                noTelp = etNoTelp.text.toString().trim(),
                email = etEmail.text.toString().trim(),
                password = etPassword.text.toString().trim(),
                tglLahir = etTanggalLahir.text.toString().trim(),
                jenisKelamin = acJenisKelamin.text.toString().trim()
            )

            val fragTwo = AlamatFragment()
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
        viewModel = ViewModelProvider(requireActivity()).get(PendaftaranViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_pribadi, container, false)
    }




}