package com.example.akhleshkumar.homedoot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.akhleshkumar.homedoot.R

class ProfileFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var myOrdersButton: Button
    private lateinit var logoutButton: Button
    private lateinit var profileImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        nameTextView = view.findViewById(R.id.nameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        phoneTextView = view.findViewById(R.id.phoneTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        myOrdersButton = view.findViewById(R.id.myOrdersButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        profileImage = view.findViewById(R.id.profileImage)

        // Set up button click listeners
        editProfileButton.setOnClickListener {
            // Handle edit/update profile action
        }

        myOrdersButton.setOnClickListener {
            // Handle my orders action
        }

        logoutButton.setOnClickListener {
            // Handle logout action
        }

        return view
    }
}
