package com.example.inclob

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PendaftaranActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendaftaran)
        getWindow().setStatusBarColor(getResources().getColor(R.color.black))
        supportActionBar?.title = "             Informasi Pribadi"

        // Tambahkan tombol kembali (optional)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


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
