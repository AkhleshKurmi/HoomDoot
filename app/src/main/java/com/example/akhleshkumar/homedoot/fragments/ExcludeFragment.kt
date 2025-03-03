package com.example.akhleshkumar.homedoot.fragments

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.akhleshkumar.homedoot.R


class ExcludeFragment : Fragment() {
 lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        return inflater.inflate(R.layout.fragment_exclude, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       textView = view.findViewById<TextView>(R.id.tv_exclude)
        arguments?.let {
            textView.text = Html.fromHtml(it.getString("text_key"),Html.FROM_HTML_MODE_LEGACY)
        }
    }

}