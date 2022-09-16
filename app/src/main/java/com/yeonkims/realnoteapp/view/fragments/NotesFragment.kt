package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNotesBinding
import com.yeonkims.realnoteapp.logic.viewmodels.NotesViewModel
import com.yeonkims.realnoteapp.view.dialogs.CreateNoteDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment() {
    @Inject
    lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNotesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes, container, false)

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_menu -> {
                        CreateNoteDialog().show(
                            parentFragmentManager, CreateNoteDialog.TAG
                        )
                    }
                    else -> return false
                }
                return true
            }

        })
    }
}