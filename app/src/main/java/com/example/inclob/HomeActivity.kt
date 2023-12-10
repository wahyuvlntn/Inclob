package com.example.inclob

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.inclob.Menu.MenuAkunFragment
import com.example.inclob.Menu.MenuBerandaFragment
import com.example.inclob.Menu.MenuPekerjaanFragment
import com.example.inclob.Menu.MenuPelatihanFragment
import com.example.inclob.Menu.MenuRiwayatFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var tvNama: TextView
    private lateinit var tvEmail: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var navigation: BottomNavigationView
    private var content: FrameLayout? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_beranda -> {
                val fragment = MenuBerandaFragment.newInstance()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_pekerjaan -> {
                val fragment = MenuPekerjaanFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_pelatihan -> {
                val fragment = MenuPelatihanFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_riwayat -> {
                val fragment = MenuRiwayatFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_akun -> {
                val fragment = MenuAkunFragment()
                addFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        getWindow().setStatusBarColor(getResources().getColor(R.color.black))
        navigation = findViewById(R.id.bottom_navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val fragment = MenuBerandaFragment.newInstance()
        addFragment(fragment)


    }
}