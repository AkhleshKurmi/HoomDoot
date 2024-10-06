package com.example.akhleshkumar.homedoot

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.akhleshkumar.homedoot.activities.CartActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView : BottomNavigationView
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        id = intent.getIntExtra("id",0)
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
            }else if(itemId == R.id.navigation_home){
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
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