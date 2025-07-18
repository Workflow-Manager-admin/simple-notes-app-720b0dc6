package com.example.notesfrontend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * Data class representing a Note entity.
 */
data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// PUBLIC_INTERFACE
/**
 * NotesViewModel — holds and manipulates app notes state, using SupabaseNoteService.
 */
class NotesViewModel : ViewModel() {
    private val notesService = SupabaseNoteService()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()

    init {
        refreshNotes()
    }

    // PUBLIC_INTERFACE
    /**
     * Fetch notes from backend and updates notes StateFlow.
     */
    fun refreshNotes() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = notesService.fetchNotes()
                _notes.value = result.sortedByDescending { it.createdAt }
            } catch (e: Exception) {
                _error.value = "Failed to load notes: ${e.message}"
            }
            _loading.value = false
        }
    }

    // PUBLIC_INTERFACE
    /**
     * Sets the currently selected note.
     */
    fun selectNote(note: Note?) = viewModelScope.launch {
        _selectedNote.value = note
    }

    // PUBLIC_INTERFACE
    /**
     * Creates a new note by calling the backend.
     */
    fun createNote(title: String, content: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                notesService.createNote(title, content)
                refreshNotes()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to create note: ${e.message}"
            }
            _loading.value = false
        }
    }

    // PUBLIC_INTERFACE
    /**
     * Updates an existing note.
     */
    fun updateNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                notesService.updateNote(note)
                refreshNotes()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to update note: ${e.message}"
            }
            _loading.value = false
        }
    }

    // PUBLIC_INTERFACE
    /**
     * Deletes a note.
     */
    fun deleteNote(note: Note, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                notesService.deleteNote(note)
                refreshNotes()
                onSuccess()
            } catch (e: Exception) {
                _error.value = "Failed to delete note: ${e.message}"
            }
            _loading.value = false
        }
    }
}
