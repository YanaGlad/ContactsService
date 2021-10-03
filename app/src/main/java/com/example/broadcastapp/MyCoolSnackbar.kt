package com.example.broadcastapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.broadcastapp.databinding.NoPermissionSnackbarBinding
import com.google.android.material.snackbar.Snackbar

class MyCoolSnackbar(
    private val layoutInflater: LayoutInflater,
    private val root: ViewGroup,
    private val message: String
) {

    fun makeSnackBar() : Snackbar {
        val snackBar = Snackbar.make(root, message, Snackbar.LENGTH_LONG)
        val snackBinding = NoPermissionSnackbarBinding.inflate(layoutInflater)
        snackBinding.noPerm.text = message
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(snackBinding.root, 0)
        return snackBar
    }
}