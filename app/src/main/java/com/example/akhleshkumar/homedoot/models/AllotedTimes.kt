package com.example.akhleshkumar.homedoot.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllotedTimes(
    val default_slot: List<String>,
    val slots: List<String>,
    val time: String
) : Parcelable