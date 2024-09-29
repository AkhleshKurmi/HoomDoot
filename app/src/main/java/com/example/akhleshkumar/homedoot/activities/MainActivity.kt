package com.example.akhleshkumar.homedoot.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.fragments.CartFragment
import com.example.akhleshkumar.homedoot.fragments.HomeDootFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val homeDootFragment = HomeDootFragment()
        loadFragment(homeDootFragment)

        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            var fragment: Fragment? = null
            val itemId = item.itemId

            if (itemId == R.id.navigation_hd) {
                fragment = homeDootFragment
            } else if (itemId == R.id.navigation_home) {
                fragment = homeDootFragment
            } else if (itemId == R.id.navigation_Account) {

            }
            else if (itemId == R.id.navigation_cart) {
                fragment = CartFragment()
            }
            loadFragment(fragment)
        })

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.container, fragment)
            ft.commit()



            return true
        }
        return false
    }
}