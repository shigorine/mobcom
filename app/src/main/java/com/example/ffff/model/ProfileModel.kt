package com.example.ffff.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize
@Parcelize()
data class ProfileModel(
    @get:Exclude
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    var money: String? = null,

):Parcelable
