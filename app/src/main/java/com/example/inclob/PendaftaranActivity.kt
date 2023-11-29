package com.example.inclob

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.inclob.data.User

class PendaftaranActivity : AppCompatActivity() {
    private lateinit var viewModel: PendaftaranViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        supportActionBar?.title = "             Informasi Pribadi"

        // Tambahkan tombol kembali (optional)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(PendaftaranViewModel::class.java)


        val fragOne = InfoPribadiFragment()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_main, fragOne)
            commit()
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

//class PendaftaranViewModel: ViewModel(){
//    var userData: User? = null
//    fun saveUserData(
//        nama: String,
//        username: String,
//        noTelp: String,
//        email: String,
//        password: String,
//        tglLahir: String,
//        jenisKelamin: String,
//        provinsi: String,
//        kota: String,
//        alamat: String,
//        kecamatan: String,
//        jenisDisabilitas: String,
//        keteranganDisabilitas: String,
//        posisi: String,
//        perusahaan: String,
//        thnMulaiKerja: String,
//        blnMulaiKerja: String,
//        thnSelesaiKerja: String,
//        blnSelesaiKerja: String,
//        sekolah: String,
//        jurusan: String,
//        gelar:String,
//        thnMulaiSekolah: String,
//        thnLulusSekolah: String,
//    ) {
//        userData = User(nama, email, username, password, tglLahir, noTelp, jenisKelamin, provinsi, kota, alamat, kecamatan, jenisDisabilitas, keteranganDisabilitas, posisi, perusahaan, thnMulaiKerja, blnMulaiKerja, thnSelesaiKerja, blnSelesaiKerja, sekolah, jurusan, gelar, thnMulaiSekolah, thnLulusSekolah)
//    }
//
//    fun saveUserData(nama: String, username: String, noTelp: String, email: String, password: String, tglLahir: String, jenisKelamin: String) {
//        userData = User (nama,email,username,password,tglLahir,noTelp,jenisKelamin,"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","")
//    }
//
//
//}
class PendaftaranViewModel : ViewModel() {
    var userData: User = User("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "","")
        private set
    val dataSavedEvent = MutableLiveData<Boolean>()

    fun saveUserData(
        nama: String,
        username: String,
        noTelp: String,
        email: String,
        password: String,
        tglLahir: String,
        jenisKelamin: String
    ) {
        userData = userData.copy(
            nama = nama,
            username = username,
            noTelp = noTelp,
            email = email,
            password = password,
            tglLahir = tglLahir,
            jenisKelamin = jenisKelamin
            // tambahkan properti lain sesuai kebutuhan
        )
        dataSavedEvent.value = true

        Log.d("PendaftaranViewModel", "Data saved: $userData")
    }
}
