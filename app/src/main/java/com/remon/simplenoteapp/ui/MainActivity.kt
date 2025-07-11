// File: app/src/main/java/com/remon/simplenoteapp/ui/MainActivity.kt
package com.remon.simplenoteapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.remon.simplenoteapp.databinding.ActivityMainBinding
import com.remon.simplenoteapp.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // Mengamati perubahan pada LiveData allNotes
        noteViewModel.allNotes.observe(this) { notes ->
            // Update data di adapter setiap kali ada perubahan
            notes?.let { noteAdapter.submitList(it) }
        }

        // Aksi untuk Floating Action Button (FAB)
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this, NoteAddEditActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan fungsionalitas swipe-to-delete
        setupSwipeToDelete()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { note ->
            // Aksi saat item di-klik (mode edit)
            val intent = Intent(this, NoteAddEditActivity::class.java)
            intent.putExtra(NoteAddEditActivity.EXTRA_NOTE, note)
            startActivity(intent)
        }
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = noteAdapter
        }
    }

    private fun setupSwipeToDelete() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Tidak digunakan
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Dapatkan note yang di-swipe
                val position = viewHolder.adapterPosition
                val note = noteAdapter.currentList[position]
                // Hapus note dari ViewModel
                noteViewModel.delete(note)

                // Tampilkan Snackbar dengan opsi Undo
                Snackbar.make(binding.root, "Catatan dihapus", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        // Jika Undo di-klik, insert kembali note yang dihapus
                        noteViewModel.insert(note)
                    }.show()
            }
        }).attachToRecyclerView(binding.rvNotes)
    }
}
