package com.example.akhleshkumar.homedoot.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.akhleshkumar.homedoot.fragments.ExcludeFragment
import com.example.akhleshkumar.homedoot.fragments.IncludeFragment
import com.example.akhleshkumar.homedoot.fragments.OthersFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity, private val includeText :String, private val excludeText:String, private val otherText:String) : FragmentStateAdapter(fragmentActivity) {
    private val fragmentList = listOf(IncludeFragment(), ExcludeFragment(), OthersFragment())

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> IncludeFragment()
            1 -> ExcludeFragment()
            2 -> OthersFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
        // Pass the text to the fragment as arguments
        fragment.arguments = Bundle().apply {
            putString("text_key", if (position == 0) includeText else if(position==1) excludeText else otherText)
        }
        return fragment
    }
}
