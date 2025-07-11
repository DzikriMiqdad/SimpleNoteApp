// File: app/src/main/java/com/remon/simplenoteapp/database/Note.kt
package com.remon.simplenoteapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Kelas data ini merepresentasikan satu entitas (tabel) dalam database Room.
 * Setiap properti dalam kelas ini adalah sebuah kolom di dalam tabel.
 * @Entity menandakan bahwa kelas ini adalah sebuah tabel database.
 * @PrimaryKey menandakan bahwa 'id' adalah kunci utama. autoGenerate = true membuat ID unik secara otomatis.
 * @Parcelize memungkinkan objek dari kelas ini untuk dikirim antar Activity melalui Intent.
 */
@Entity(tableName = "note_table")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String
) : Parcelable
