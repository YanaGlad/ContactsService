package com.example.broadcastapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.broadcastapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: ContactsAdapter

    var startForResult = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val list = result.data?.extras?.getParcelableArrayList<ContactModel>(CONTACT_NAME)
            list?.let { loadContacts(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLaunch.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startForResult.launch(intent);
        }

        initRecycler()

    }

    private fun initRecycler() {
        binding.contactList.layoutManager = LinearLayoutManager(this)
        binding.contactList.setHasFixedSize(false)
        adapter = ContactsAdapter()
        binding.contactList.adapter = adapter
    }

    fun loadContacts(list: List<ContactModel>) {
        adapter.submitList(list)
        binding.btnLaunch.isEnabled = false
    }
}
