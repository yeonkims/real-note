package com.yeonkims.realnoteapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yeonkims.realnoteapp.databinding.FragmentCreateNoteDialogBinding

class CreateNoteDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCreateNoteDialogBinding
    private val viewModel = CreateNoteDialogViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_create_note_dialog, null, false);

        binding.createNoteDialogViewModel = viewModel
        binding.lifecycleOwner = this

        binding.okBtn.setOnClickListener {
            viewModel.createNote()
            dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object {
        const val TAG = "CreateNoteDialog"
    }

}