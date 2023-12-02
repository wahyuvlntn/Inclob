package com.example.inclob.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inclob.R
import com.example.inclob.pelatihanFrag.DetailPelatihanFragment

class PelatihanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelatihan)

        val docId = intent.getStringExtra("doc_id")
        val fragOne = DetailPelatihanFragment.newInstance(docId)
        supportFragmentManager.beginTransaction().apply {
            //fragment melamar
            replace(R.id.content, fragOne)
            commit()
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}