package com.remon.simplenoteapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                    // BARU: Menambahkan callback saat database dibuat
                    .addCallback(roomCallback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // BARU: Membuat RoomDatabase.Callback untuk mengisi data awal
        private val roomCallback = object : RoomDatabase.Callback() {
            /**
             * Metode ini hanya dipanggil SATU KALI saat database pertama kali dibuat.
             * Sempurna untuk mengisi data awal (pre-populate).
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Kita menggunakan coroutine untuk menjalankan operasi database di background thread
                CoroutineScope(Dispatchers.IO).launch {
                    val dao = INSTANCE?.noteDao()
                    if (dao != null) {
                        // Catatan pertama
                        val note1 = Note(
                            title = "Dzikri Miqdad Alhamdani",
                            content = "NIM : 312310251\nKelas : TI.23.C4"
                        )
                        dao.insert(note1)

                        // Catatan kedua
                        val note2 = Note(
                            title = "Pemograman Mobile 2",
                            content = "Dosen :\nEko Budiarto, S.Kom, M.M"
                        )
                        dao.insert(note2)
                    }
                }
            }
        }
    }
}
