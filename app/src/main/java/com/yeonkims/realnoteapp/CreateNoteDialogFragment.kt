package com.yeonkims.realnoteapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.yeonkims.realnoteapp.databinding.ActivityMainBinding
import com.yeonkims.realnoteapp.databinding.FragmentCreateNoteDialogBinding

class CreateNoteDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentCreateNoteDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_create_note_dialog, container, false)
//        return view
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_create_note_dialog, null, false);
        return binding.root
    }

    companion object {
        const val TAG = "CreateNoteDialog"
    }

}