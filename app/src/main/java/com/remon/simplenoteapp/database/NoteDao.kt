// File: app/src/main/java/com/remon/simplenoteapp/database/NoteDao.kt
package com.remon.simplenoteapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * DAO (Data Access Object) mendefinisikan metode untuk berinteraksi dengan database.
 * Room akan mengimplementasikan metode-metode ini secara otomatis.
 * @Dao menandakan bahwa ini adalah sebuah interface DAO.
 */
@Dao
interface NoteDao {

    /**
     * Menyisipkan satu catatan ke dalam tabel.
     * OnConflictStrategy.REPLACE berarti jika ada catatan dengan ID yang sama, catatan lama akan diganti.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    /**
     * Memperbarui catatan yang sudah ada di tabel.
     */
    @Update
    suspend fun update(note: Note)

    /**
     * Menghapus satu catatan dari tabel.
     */
    @Delete
    suspend fun delete(note: Note)

    /**
     * Mengambil semua catatan dari tabel, diurutkan berdasarkan ID secara menurun (yang terbaru di atas).
     * Mengembalikan LiveData, sehingga UI bisa otomatis update jika ada perubahan data.
     */
    @Query("SELECT * FROM note_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}
