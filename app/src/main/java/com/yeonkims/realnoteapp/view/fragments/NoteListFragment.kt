package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNoteListBinding
import com.yeonkims.realnoteapp.logic.viewmodels.NotesViewModel
import com.yeonkims.realnoteapp.view.dialogs.CreateNoteDialog
import com.yeonkims.realnoteapp.view.recyclerViewAdapter.NoteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    @Inject
    lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentNoteListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_list, container, false)

        val adapter = NoteListAdapter(viewModel)

        val activity  = requireActivity()

        activity.setActionBar(binding.toolbar)

        binding.toolbar.inflateMenu(R.menu.note_list_menu)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.create_menu -> {
                        if(viewModel.isLoading.value == false) {
                            CreateNoteDialog().show(
                                parentFragmentManager, CreateNoteDialog.TAG
                            )
                        }
                    }
                    else -> return false
                }
                return true
            }

        })
        binding.notesViewModel = viewModel
        binding.noteList.adapter = adapter
        viewModel.latestNotes.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        binding.lifecycleOwner = this

        return binding.root
    }

}