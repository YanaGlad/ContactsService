package com.example.broadcastapp


import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class SecondActivity : AppCompatActivity() {
    private var bound = false
    private var contactsService: ContactsService? = null

    private var connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder = service as ContactsService.LocalContactsBinder
            contactsService = binder.service
            bound = true
        }
        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    private val contactsReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val noPerm = intent.getBooleanExtra(NO_PERMISSION_EXTRA, false)
            val message = intent.getStringArrayListExtra(CONTACT_NAME_EXTRA)
            val data = Intent()
            data.putStringArrayListExtra(CONTACT_NAME_EXTRA, message)
            if (noPerm)
                setResult(RESULT_CANCELED, data)
            else
                setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        LocalBroadcastManager.getInstance(this).registerReceiver(
            contactsReceiver,
            IntentFilter(CUSTOM_FILTER_CONTACT_INTENT)
        )
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, ContactsService::class.java)
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(contactsReceiver)
    }
}