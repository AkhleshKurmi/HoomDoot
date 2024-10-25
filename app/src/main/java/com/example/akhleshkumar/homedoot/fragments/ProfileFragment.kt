package com.example.akhleshkumar.homedoot.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.akhleshkumar.homedoot.R
import com.example.akhleshkumar.homedoot.activities.LoginActivity
import com.example.akhleshkumar.homedoot.activities.OrderListActivity
import com.example.akhleshkumar.homedoot.activities.UpdatefrofileActivity

class ProfileFragment : Fragment() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var editProfileButton: Button
    private lateinit var myOrdersButton: Button
    private lateinit var logoutButton: Button
    private lateinit var profileImage: ImageView
    var userId = " "
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editorSP : SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferences = requireContext().getSharedPreferences("HomeDoot",
            AppCompatActivity.MODE_PRIVATE
        )
        editorSP = sharedPreferences.edit()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nameTextView = view.findViewById(R.id.nameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)
        phoneTextView = view.findViewById(R.id.phoneTextView)
        editProfileButton = view.findViewById(R.id.editProfileButton)
        myOrdersButton = view.findViewById(R.id.myOrdersButton)
        logoutButton = view.findViewById(R.id.logoutButton)
        profileImage = view.findViewById(R.id.profileImage)
        userId = requireArguments().getInt("id",0).toString()
        nameTextView.text= requireArguments().getString("name","")
        emailTextView.text = requireArguments().getString("email","")
        phoneTextView.text = requireArguments().getString("mobile","")
        // Set up button click listeners
        editProfileButton.setOnClickListener {
            startActivity(Intent(requireContext(), UpdatefrofileActivity::class.java).putExtra("userId",userId))
        }

        myOrdersButton.setOnClickListener {
            startActivity(Intent(requireContext(),OrderListActivity::class.java).putExtra("userId",userId))
        }

        logoutButton.setOnClickListener {
            editorSP.putBoolean("isLogin",false)
            editorSP.clear()
            editorSP.commit()
            requireContext().startActivity(Intent(requireContext(),LoginActivity::class.java))
            requireActivity().finish()
        }

    }

    override fun onResume() {
        super.onResume()
        userId = requireArguments().getInt("id",0).toString()
        nameTextView.text= requireArguments().getString("name","")
        emailTextView.text = requireArguments().getString("email","")
        phoneTextView.text = requireArguments().getString("mobile","")
    }
}
