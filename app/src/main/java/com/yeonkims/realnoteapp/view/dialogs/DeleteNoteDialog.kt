package com.yeonkims.realnoteapp.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.databinding.FragmentDeleteNoteDialogBinding
import com.yeonkims.realnoteapp.logic.viewmodels.DeleteNoteDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeleteNoteDialog(val note: Note?) : DialogFragment() {
    private lateinit var binding: FragmentDeleteNoteDialogBinding

    @Inject
    lateinit var viewModel : DeleteNoteDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_delete_note_dialog, null, false);

        binding.deleteNoteDialogViewModel = viewModel
        binding.lifecycleOwner = this

        binding.okBtn.setOnClickListener {
            if(note != null) {
                viewModel.deleteNote(note)
            }
            requireParentFragment().findNavController().popBackStack()
            dismiss()
        }

        binding.cancelBtn.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    companion object {
        const val TAG = "DeleteNoteDialog"
    }

}