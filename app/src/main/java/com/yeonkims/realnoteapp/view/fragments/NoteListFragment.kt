package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.databinding.FragmentNoteListBinding
import com.yeonkims.realnoteapp.logic.viewmodels.auth.AuthViewModel
import com.yeonkims.realnoteapp.logic.viewmodels.note.NotesViewModel
import com.yeonkims.realnoteapp.util.extension_functions.safeNavigate
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
    lateinit var binding: FragmentNoteListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_note_list, container, false
        )
        val navController = findNavController()

        observeIsLoggedIn(navController)
        initDrawer()
        setDrawerMenu()
        setNoteList()
        setFloatingButtonOnClick(navController)

        binding.notesViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun observeIsLoggedIn(navController: NavController) {
        viewModel.currentUser.observe(viewLifecycleOwner) { currentUser ->
            if (currentUser == null) {
                Log.i(javaClass.simpleName, javaClass.simpleName)
                val action = NoteListFragmentDirections.actionNoteListFragmentToLoginFragment()
                navController.safeNavigate(action)
            }
        }
    }

    private fun initDrawer() {
        val activity = requireActivity()

        val drawerLayout = binding.drawerLayout
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            activity, drawerLayout, binding.toolbar, R.string.open, R.string.close)

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun setDrawerMenu() {
        val navigationView = binding.navView
        val fragmentManger = parentFragmentManager

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
    }

    private fun setNoteList() {
        val adapter = NoteListAdapter(viewModel)
        binding.noteList.adapter = adapter
        viewModel.latestNotes.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun setFloatingButtonOnClick(navController: NavController) {
        binding.addNoteBtn.setOnClickListener {
            val action = NoteListFragmentDirections
                .actionNoteListFragmentToSelectedNoteFragment(null)
            navController.safeNavigate(action)
        }
    }

}