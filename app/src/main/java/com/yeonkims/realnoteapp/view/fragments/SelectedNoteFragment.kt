package com.yeonkims.realnoteapp.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yeonkims.realnoteapp.R
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.databinding.FragmentSelectedNoteBinding
import com.yeonkims.realnoteapp.logic.viewmodels.note.SelectedNoteViewModel
import com.yeonkims.realnoteapp.util.extension_functions.hideKeyboard
import com.yeonkims.realnoteapp.view.dialogs.DeleteNoteDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SelectedNoteFragment : Fragment() {

    @Inject
    lateinit var viewModelAssistedFactory: SelectedNoteViewModel.Factory
    private lateinit var callback: OnBackPressedCallback

    val viewModel: SelectedNoteViewModel by viewModels {

        val args: SelectedNoteFragmentArgs by navArgs()
        SelectedNoteViewModel.provideFactory(viewModelAssistedFactory, args)
    }

    private lateinit var menuProvider: SelectedNoteMenuProvider
    lateinit var binding : FragmentSelectedNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_selected_note, container, false
        )

        setToolbar()
        setBackButtonOnClick()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.saveNote()
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        callback.remove()
        super.onDetach()
    }

    private fun setToolbar() {
        val activity = requireActivity()
        activity.setActionBar(binding.toolbar)

        binding.toolbar.inflateMenu(R.menu.selected_note_menu)
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    private fun setBackButtonOnClick() {
        val navController = findNavController()
        binding.toolbar.setNavigationOnClickListener {
            viewModel.saveNote()
            navController.popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val note = viewModel.note
        val menuHost: MenuHost = requireActivity()
        menuProvider = SelectedNoteMenuProvider(note)
        menuHost.addMenuProvider(menuProvider)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val menuHost: MenuHost = requireActivity()
        menuHost.removeMenuProvider(menuProvider)
    }

    inner class SelectedNoteMenuProvider(
        private val note: Note?
    ) : MenuProvider {

        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.selected_note_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.delete_menu -> {
                    DeleteNoteDialog(note).show(
                        parentFragmentManager, DeleteNoteDialog.TAG
                    )
                }
                R.id.done_menu -> {
                    viewModel.saveNote()
                    hideKeyboard()
                }
            }
            return true
        }

    }
}

