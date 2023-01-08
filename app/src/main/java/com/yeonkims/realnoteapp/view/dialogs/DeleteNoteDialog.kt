package com.yeonkims.realnoteapp.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentDeleteNoteDialogBinding
import com.yeonkims.realnoteapp.logic.viewmodels.note.SelectedNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteNoteDialog(val viewModel: SelectedNoteViewModel) : DialogFragment() {
    private lateinit var binding: FragmentDeleteNoteDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_delete_note_dialog, null, false);

        setOnClickListeners()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.apply {
            okBtn.setOnClickListener {
                this@DeleteNoteDialog.viewModel.deleteNote()
                requireParentFragment().findNavController().popBackStack()
                dismiss()
            }
            cancelBtn.setOnClickListener {
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "DeleteNoteDialog"
    }

}