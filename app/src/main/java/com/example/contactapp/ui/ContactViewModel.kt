package com.example.contactapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp.data.ContactRepository
import com.example.contactapp.model.Contact
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ContactRepository(application)

    val contactList = mutableStateListOf<Contact>()

    init {
        viewModelScope.launch {
            contactList.addAll(repository.getAllContacts())
        }
    }
}