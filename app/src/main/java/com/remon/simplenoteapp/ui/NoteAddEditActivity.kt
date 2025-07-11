// File: app/src/main/java/com/remon/simplenoteapp/ui/NoteAddEditActivity.kt
package com.remon.simplenoteapp.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.remon.simplenoteapp.R
import com.remon.simplenoteapp.database.Note
import com.remon.simplenoteapp.databinding.ActivityNoteAddEditBinding
import com.remon.simplenoteapp.viewmodel.NoteViewModel

class NoteAddEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteAddEditBinding
    private lateinit var noteViewModel: NoteViewModel
    private var currentNote: Note? = null

    companion object {
        const val EXTRA_NOTE = "extra_note"
        private const val TAG = "NoteAddEditActivity" // Tag untuk logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        try {
            if (intent.hasExtra(EXTRA_NOTE)) {
                // PERBAIKAN: Menggunakan metode yang sesuai dengan versi Android
                currentNote = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Untuk Android 13 (API 33) dan yang lebih baru
                    intent.getParcelableExtra(EXTRA_NOTE, Note::class.java)
                } else {
                    // Untuk versi Android di bawah 13
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(EXTRA_NOTE)
                }

                if (currentNote != null) {
                    binding.etTitle.setText(currentNote?.title)
                    binding.etContent.setText(currentNote?.content)
                    supportActionBar?.title = "Edit Catatan"
                } else {
                    Log.e(TAG, "Gagal mengambil Note dari Intent, meskipun extra ada.")
                    Toast.makeText(this, "Gagal memuat catatan.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                supportActionBar?.title = "Tambah Catatan"
                invalidateOptionsMenu()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saat memuat data catatan", e)
            Toast.makeText(this, "Terjadi error saat memuat data.", Toast.LENGTH_SHORT).show()
            finish()
        }


        binding.btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val note = Note(
            id = currentNote?.id ?: 0,
            title = title,
            content = content
        )

        if (currentNote != null) {
            noteViewModel.update(note)
            Toast.makeText(this, "Catatan diperbarui", Toast.LENGTH_SHORT).show()
        } else {
            noteViewModel.insert(note)
            Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (currentNote != null) {
            menuInflater.inflate(R.menu.menu_add_edit_note, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hapus Catatan")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")
        builder.setPositiveButton("Hapus") { _, _ ->
            currentNote?.let {
                noteViewModel.delete(it)
                Toast.makeText(this, "Catatan dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        builder.setNegativeButton("Batal", null)
        builder.show()
    }
}
