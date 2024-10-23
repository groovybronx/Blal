package com.example.contactapp.data

import android.content.Context
import android.provider.ContactsContract
import com.example.contactapp.model.Contact

class ContactRepository(private val context: Context) {

    fun getAllContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                var phoneNumber: String? = null
                if (hasPhoneNumber > 0) {
                    val phoneCursor = context.contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id.toString()),
                        null
                    )
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        phoneCursor.close()
                    }
                }
                contactList.add(Contact(id, name, phoneNumber))
            } while (cursor.moveToNext())
            cursor.close()
        }
        return contactList
    }
}