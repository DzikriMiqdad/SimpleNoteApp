// File: app/src/main/java/com/remon/simplenoteapp/viewmodel/NoteViewModel.kt
package com.remon.simplenoteapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.remon.simplenoteapp.database.Note
import com.remon.simplenoteapp.database.NoteDatabase
import com.remon.simplenoteapp.repository.NoteRepository
import kotlinx.coroutines.launch

/**
 * ViewModel menyediakan data untuk UI dan bertahan dari perubahan konfigurasi (misal: rotasi layar).
 * Ia bertindak sebagai penghubung antara Repository dan UI.
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        // Inisialisasi DAO, Database, dan Repository
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    /**
     * Meluncurkan coroutine baru untuk menyisipkan data tanpa memblokir UI.
     */
    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    /**
     * Meluncurkan coroutine baru untuk memperbarui data.
     */
    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    /**
     * Meluncurkan coroutine baru untuk menghapus data.
     */
    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }
}
