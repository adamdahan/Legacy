package com.adamdahan.cibc.legacy.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.adamdahan.cibc.legacy.UserService
import com.adamdahan.cibc.legacy.UserViewModel

/**
 * Legacy Android Activity demonstrating KMP integration
 * 
 * Flow:
 * 1. Traditional XML Layout (activity_legacy.xml)
 * 2. ComposeView in the XML layout
 * 3. Jetpack Compose UI (UserListScreen)
 * 4. KMP ViewModel (UserViewModel)
 * 5. KMP Service (UserService)
 * 6. KMP Data Model (User)
 * 
 * This mirrors the iOS architecture:
 * XML Activity ≈ Objective-C ViewController
 * ComposeView ≈ UIHostingController
 * Jetpack Compose ≈ SwiftUI
 */
class LegacyActivity : ComponentActivity() {
    
    // KMP ViewModel
    private lateinit var viewModel: UserViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the traditional XML layout
        setContentView(R.layout.activity_legacy)
        
        // Initialize KMP ViewModel
        setupViewModel()
        
        // Setup Jetpack Compose in the ComposeView
        setupComposeView()
        
        Log.d("LegacyActivity", "✅ Legacy Activity created with KMP integration")
    }
    
    /**
     * Initialize the KMP ViewModel
     * This is the entry point to the Kotlin Multiplatform code
     */
    private fun setupViewModel() {
        // Create an instance of the KMP ViewModel
        val userService = UserService()
        viewModel = UserViewModel(userService)
        
        Log.d("LegacyActivity", "✅ KMP ViewModel initialized")
    }
    
    /**
     * Setup Jetpack Compose in the ComposeView from XML
     * This bridges traditional Android Views with Jetpack Compose
     */
    private fun setupComposeView() {
        // Find the ComposeView from the XML layout
        val composeView = findViewById<ComposeView>(R.id.composeView)
        
        // Configure the ComposeView
        composeView.apply {
            // Set composition strategy to dispose when the view is detached
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            
            // Set the Compose content
            setContent {
                MyApplicationTheme {
                    // Display the user list with KMP ViewModel
                    UserListScreen(viewModel = viewModel)
                }
            }
        }
        
        Log.d("LegacyActivity", "✅ Jetpack Compose integrated in XML layout")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d("LegacyActivity", "🗑️ LegacyActivity destroyed")
    }
}

