package com.example.broadcastapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.broadcastapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ContactsAdapter

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            val intent = Intent(this, SecondActivity::class.java)
            startForResult.launch(intent)
        }

    private var startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val list = result.data?.extras?.getParcelableArrayList<ContactModel>(CONTACT_NAME_EXTRA)
            list?.let { loadContacts(it) }
        } else
            MyCoolSnackbar(
                layoutInflater,
                binding.root,
                getString(R.string.no_permission)
            )
                .makeSnackBar()
                .show()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLaunchSecondActivity.setOnClickListener {
            activityResultLauncher.launch(Manifest.permission.READ_CONTACTS)
        }

        initRecycler()
    }


    private fun initRecycler() {
        adapter = ContactsAdapter()
        binding.contactList.adapter = adapter
    }

    private fun loadContacts(list: List<ContactModel>) {
        adapter.submitList(list)
        binding.buttonLaunchSecondActivity.isEnabled = false
    }
}
