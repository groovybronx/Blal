package com.example.contactapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel


@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: ContactViewModel = viewModel()) {
    val context = LocalContext.current
    var hasPermission by remember { mutableStateOf(false) }

    // Launcher for requesting permission
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            hasPermission = isGranted
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Contacts") })
        }
    ) { innerPadding ->
        if (hasPermission) {
            LazyColumn(contentPadding = innerPadding) {
                items(viewModel.contactList) { contact ->
                    Text(text = "${contact.name}: ${contact.phoneNumber}")
                }
            }
        } else {
            Button(onClick = {
                // Check if permission is already granted
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_CONTACTS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    hasPermission = true
                } else {
                    // Launch the permission request
                    launcher.launch(Manifest.permission.READ_CONTACTS)
                }
            }) {
                Text("Accorder l'autorisation")
            }
        }
    }

    // Fetch contacts when permission is granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.contactList.addAll(ContactRepository(context).getAllContacts())
        }
    }
}