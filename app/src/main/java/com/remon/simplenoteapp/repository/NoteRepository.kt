// File: app/src/main/java/com/remon/simplenoteapp/repository/NoteRepository.kt
package com.remon.simplenoteapp.repository

import androidx.lifecycle.LiveData
import com.remon.simplenoteapp.database.Note
import com.remon.simplenoteapp.database.NoteDao

/**
 * Repository adalah kelas yang mengabstraksi akses ke berbagai sumber data (dalam kasus ini, hanya Room).
 * Ini adalah perantara antara ViewModel dan sumber data.
 */
class NoteRepository(private val noteDao: NoteDao) {

    // Mengambil semua catatan dari DAO. LiveData akan menangani update secara otomatis.
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    // Fungsi suspend untuk menyisipkan data. Harus dipanggil dari coroutine.
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    // Fungsi suspend untuk memperbarui data.
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    // Fungsi suspend untuk menghapus data.
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
