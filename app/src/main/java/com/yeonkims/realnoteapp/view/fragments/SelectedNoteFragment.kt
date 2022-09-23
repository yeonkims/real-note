package com.yeonkims.realnoteapp.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.databinding.FragmentSelectedNoteBinding
import com.yeonkims.realnoteapp.logic.viewmodels.SelectedNoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectedNoteFragment : Fragment() {

    @Inject
    lateinit var viewModel: SelectedNoteViewModel

    private lateinit var menuProvider: SelectedNoteMenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args: SelectedNoteFragmentArgs by navArgs()

        val binding : FragmentSelectedNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_selected_note, container, false)

        val note = args.selectedNote
        binding.note = note

        binding.lifecycleOwner = this

        val activity  = requireActivity()

        activity.setActionBar(binding.toolbar)
        binding.toolbar.title = ""
        binding.toolbar.inflateMenu(R.menu.selected_note_menu)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)

        val navController = findNavController()
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        val menuHost: MenuHost = requireActivity()
        menuProvider = SelectedNoteMenuProvider(
            navController,
            note,
        )
        menuHost.addMenuProvider(menuProvider)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(menuProvider)
    }

    inner class SelectedNoteMenuProvider(
        private val navController: NavController,
        private val note: Note,
    ) : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.selected_note_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.delete_menu -> {
                    viewModel.deleteNote(note)
                    navController.popBackStack()
                }
                else -> return false
            }
            return true
        }
    }
}

