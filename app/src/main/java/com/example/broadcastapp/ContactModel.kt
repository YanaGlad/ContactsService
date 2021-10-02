package com.example.broadcastapp

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactModel(val name: String, val photo: Bitmap?) : Parcelable