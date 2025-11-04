package com.adamdahan.cibc.legacy.android

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adamdahan.cibc.legacy.User
import com.adamdahan.cibc.legacy.UserService
import com.adamdahan.cibc.legacy.UserViewModel

/**
 * Jetpack Compose UI that displays a list of users from the KMP ViewModel
 * This composable bridges Compose with the Kotlin Multiplatform code
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: UserViewModel) {
    // State to hold the list of users
    var users by remember { mutableStateOf<List<User>>(emptyList()) }

    // Load users when the composable is first composed
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
        
        // Convert KMP list to Kotlin list
        val userList = mutableListOf<User>()
        val count = viewModel.getUserCount()
        for (i in 0 until count) {
            viewModel.getUserAt(i)?.let { user ->
                userList.add(user)
            }
        }
        users = userList
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users) { user ->
                UserListItem(user = user)
            }
        }
    }
}

/**
 * Composable for displaying individual user information
 */
@Composable
fun UserListItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Preview for the user list
 */
@Preview(showBackground = true)
@Composable
fun UserListPreview() {
    MyApplicationTheme {
        val viewModel = UserViewModel(UserService())
        UserListScreen(viewModel = viewModel)
    }
}

