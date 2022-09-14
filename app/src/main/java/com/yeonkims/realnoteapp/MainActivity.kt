package com.yeonkims.realnoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yeonkims.realnoteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModel = NotesViewModel()
        binding.notesViewModel = viewModel

        //activity 관련일때 쓰면 좋음
        binding.nextBtn.setOnClickListener {
            viewModel.nextNote()
        }

        binding.previousBtn.setOnClickListener {
            viewModel.prevNote()
        }

        binding.lifecycleOwner = this
    }
}