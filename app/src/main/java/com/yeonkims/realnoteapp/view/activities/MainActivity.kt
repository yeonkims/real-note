package com.yeonkims.realnoteapp.view.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.ActivityMainBinding
import com.yeonkims.realnoteapp.view.dialogs.CreateNoteDialog
import com.yeonkims.realnoteapp.logic.viewmodels.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModel: NotesViewModel

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

        binding.deleteBtn.setOnClickListener {
            viewModel.deleteNote()
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
                CreateNoteDialog().show(
                    supportFragmentManager, CreateNoteDialog.TAG
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}