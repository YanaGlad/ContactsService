package com.example.broadcastapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

const val CUSTOM_FILTER_CONTACT_INTENT = "contact-intent"
const val CONTACT_NAME_EXTRA = "contact_name"
const val NO_PERMISSION_EXTRA = "no_permission"

fun checkReadContactsPermission(context: Context) : Boolean{
    return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
}