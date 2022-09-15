package com.yeonkims.realnoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.yeonkims.realnoteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel = NotesViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.notesViewModel = viewModel

        binding.nextBtn.setOnClickListener {
            viewModel.nextNote()
        }

        binding.previousBtn.setOnClickListener {
            viewModel.prevNote()
        }

        binding.lifecycleOwner = this
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_menu -> {
                CreateNoteDialogFragment().show(
                    supportFragmentManager, CreateNoteDialogFragment.TAG
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}