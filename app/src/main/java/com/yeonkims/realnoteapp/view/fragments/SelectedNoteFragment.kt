package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNoteListBinding
import com.yeonkims.realnoteapp.databinding.FragmentSelectedNoteBinding

class SelectedNoteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args: SelectedNoteFragmentArgs by navArgs()

        val binding : FragmentSelectedNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_selected_note, container, false)

        binding.note = args.selectedNote

        binding.lifecycleOwner = this

        return binding.root
    }

}