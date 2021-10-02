package com.example.broadcastapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.broadcastapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ContactsAdapter
    private val REQUEST_CODE = 1

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                READ_CONTACTS_GRANTED = true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_CODE
                )
            }
        }

    private var startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val list = result.data?.extras?.getParcelableArrayList<ContactModel>(CONTACT_NAME)
            list?.let { loadContacts(it) }
        } else {
            makeSnackBar(R.layout.no_permission_snackbar, getString(R.string.no_permission))
        }
    }

    private fun makeSnackBar(snackBackLayout : Int, message: String) {
        Snackbar.make(binding.main, message, Snackbar.LENGTH_LONG).show()
        val snackBar = Snackbar.make(binding.root, "", Snackbar.LENGTH_LONG)
        val customSnackView: View = layoutInflater.inflate(snackBackLayout, null)
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)

        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.setPadding(0, 0, 0, 0)
        val view = snackBar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.BOTTOM
        view.layoutParams = params
        snackBarLayout.addView(customSnackView, 0)
        snackBar.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        activityResultLauncher.launch(Manifest.permission.READ_CONTACTS)

        binding.btnLaunch.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startForResult.launch(intent)
        }
        initRecycler()
    }

    private fun initRecycler() {
        binding.contactList.layoutManager = LinearLayoutManager(this)
        binding.contactList.setHasFixedSize(false)
        adapter = ContactsAdapter()
        binding.contactList.adapter = adapter
    }

    private fun loadContacts(list: List<ContactModel>) {
        adapter.submitList(list)
        binding.btnLaunch.isEnabled = false
    }
}
