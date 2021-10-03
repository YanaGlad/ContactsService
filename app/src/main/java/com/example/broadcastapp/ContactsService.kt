package com.example.broadcastapp

import android.annotation.SuppressLint
import android.app.Service
import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.ByteArrayInputStream
import java.util.*

class ContactsService : Service() {
    private val binder: IBinder = LocalContactsBinder()

    inner class LocalContactsBinder : Binder() {
        val service: ContactsService
            get() = this@ContactsService
    }

    override fun onBind(intent: Intent?): IBinder {
        if (checkReadContactsPermission(this))
            loadContacts()
        else {
            val intent = Intent(CUSTOM_FILTER_CONTACT_INTENT).putExtra(NO_PERMISSION_EXTRA, true)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        }
        return binder
    }


    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }


    @SuppressLint("Range")
    fun loadContacts(): ArrayList<ContactModel> {
        val intent = Intent(CUSTOM_FILTER_CONTACT_INTENT)

        val contentResolver = contentResolver
        val cursor: Cursor? =
            contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        val contacts = ArrayList<ContactModel>()

        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    val name: String = getString(
                        getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                    )

                    val id = getString(
                        getColumnIndex(ContactsContract.Contacts._ID)
                    )


                    val contactUri: Uri =
                        ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI,
                            id.toLong()
                        )

                    val photoUri: Uri = Uri.withAppendedPath(
                        contactUri,
                        ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                    )

                    val photoCursor = getContentResolver().query(
                        photoUri,
                        arrayOf(ContactsContract.Contacts.Photo.PHOTO),
                        null,
                        null,
                        null
                    )

                    var photo: Bitmap? = null
                    photoCursor.use { cur ->
                        if (cur?.moveToFirst() == true) {
                            val data = cur.getBlob(0)
                            if (data != null) {
                                photo = BitmapFactory.decodeStream(ByteArrayInputStream(data))
                            }
                        }
                    }
                    contacts.add(ContactModel(name, photo))
                }
            }
            cursor.close()
        }

        intent.putParcelableArrayListExtra(CONTACT_NAME_EXTRA, contacts)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

        return contacts
    }
}