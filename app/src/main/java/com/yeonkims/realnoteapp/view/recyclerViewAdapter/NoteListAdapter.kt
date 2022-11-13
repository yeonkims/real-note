package com.yeonkims.realnoteapp.view.recyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.yeonkims.realnoteapp.databinding.ListItemNoteBinding
import com.yeonkims.realnoteapp.logic.viewmodels.note.NotesViewModel
import com.yeonkims.realnoteapp.util.extension_functions.safeNavigate
import com.yeonkims.realnoteapp.view.fragments.NoteListFragmentDirections

class NoteListAdapter(private val noteViewModel: NotesViewModel): RecyclerView.Adapter<ViewHolder>() {

    private lateinit var binding: ListItemNoteBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ListItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noteViewModel, position)
    }

    override fun getItemCount(): Int {
        if(noteViewModel.latestNotes.value == null)
            return 0
        return noteViewModel.latestNotes.value!!.size
    }

}

class ViewHolder(private val binding: ListItemNoteBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(notesViewModel: NotesViewModel, position: Int) {
        val note = notesViewModel.selectedNote(position)
        binding.note = note


        binding.noteArea.setOnClickListener {
            val action =
            NoteListFragmentDirections.actionNoteListFragmentToSelectedNoteFragment(note)
                it.findNavController().safeNavigate(action)
        }
    }
}