package com.example.broadcastapp


import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class SecondActivity : AppCompatActivity() {
    private var binded = false
    private var contactsService: ContactsService? = null
    private val REQUEST_CODE = 1

    var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ContactsService.LocalContactsBinder
            contactsService = binder.service
            binded = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            binded = false
        }
    }


    private val contactsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val message = intent.getStringArrayListExtra(CONTACT_NAME)

            val data = Intent()
            data.putStringArrayListExtra(CONTACT_NAME, message)
            setResult(RESULT_OK, data)
            finish()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val hasReadContactPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)

        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            READ_CONTACTS_GRANTED = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CODE
            )
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            contactsReceiver,
            IntentFilter(CUSTOM_FILTER_CONTACT)
        )
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, ContactsService::class.java)
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        // val handler = Handler(Looper.myLooper()!!)
        //  handler.postDelayed({ finish() }, 1000)
    }

    override fun onStop() {
        super.onStop()
        if (binded) {
            unbindService(connection)
            binded = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(contactsReceiver)
    }
}