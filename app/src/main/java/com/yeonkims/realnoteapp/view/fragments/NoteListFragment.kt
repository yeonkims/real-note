package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNoteListBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import com.yeonkims.realnoteapp.logic.viewmodels.note.NotesViewModel
import com.yeonkims.realnoteapp.view.dialogs.LogoutDialog
import com.yeonkims.realnoteapp.view.recyclerViewAdapter.NoteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class NoteListFragment : Fragment() {

    @Inject
    lateinit var viewModel: NotesViewModel
    @Inject
    lateinit var authViewModel: AuthViewModel

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
        val navController = findNavController()

        val fragmentManger = parentFragmentManager

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.note_list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when (menuItem.itemId) {
                    R.id.create_menu -> {
                        val action = NoteListFragmentDirections
                            .actionNoteListFragmentToSelectedNoteFragment(null)
                        navController.navigate(action)

                    }
                    R.id.logout_menu -> {
                        LogoutDialog().show(
                            fragmentManger, LogoutDialog.TAG
                        )
                    }
                    else -> return false
                }
                return true
            }

        })

        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if(currentUser == null) {
                Log.i(javaClass.simpleName, javaClass.simpleName)
                val action = NoteListFragmentDirections.actionNoteListFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        binding.notesViewModel = viewModel
        binding.noteList.adapter = adapter
        viewModel.latestNotes.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        binding.lifecycleOwner = this

        return binding.root
    }

}