package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNoteListBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import com.yeonkims.realnoteapp.logic.viewmodels.note.NotesViewModel
import com.yeonkims.realnoteapp.view.dialogs.LogoutDialog
import com.yeonkims.realnoteapp.view.recyclerViewAdapter.NoteListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NoteListFragment : Fragment() {

    @Inject
    lateinit var viewModel: NotesViewModel

    @Inject
    lateinit var authViewModel: AuthViewModel

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNoteListBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_list, container, false
        )

        val adapter = NoteListAdapter(viewModel)

        val activity = requireActivity()
        val navController = findNavController()

        val navigationView = binding.navView
        val fragmentManger = parentFragmentManager

        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(
            activity, drawerLayout, binding.toolbar, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        binding.toolbar.title = "Real Notes"

        binding.addNoteBtn.setOnClickListener {
            val action = NoteListFragmentDirections
                .actionNoteListFragmentToSelectedNoteFragment(null)
            navController.navigate(action)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.logout_menu -> {
                    LogoutDialog().show(
                        fragmentManger, LogoutDialog.TAG
                    )
                }
            }
            true
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser == null) {
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