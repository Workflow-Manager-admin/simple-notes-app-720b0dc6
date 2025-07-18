package com.example.notesfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesfrontend.ui.NotesNavHost
import com.example.notesfrontend.ui.theme.NotesFrontendTheme

class MainActivity : ComponentActivity() {
    // PUBLIC_INTERFACE
    /**
     * Main entry point for the app. Initializes Compose and sets the navigation host.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotesFrontendTheme {
                Surface {
                    val notesViewModel: NotesViewModel = viewModel()
                    NotesNavHost(notesViewModel)
                }
            }
        }
    }
}
