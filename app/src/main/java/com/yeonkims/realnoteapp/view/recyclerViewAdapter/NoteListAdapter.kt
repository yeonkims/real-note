package com.yeonkims.realnoteapp.view.recyclerViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.databinding.ListItemNoteBinding
import com.yeonkims.realnoteapp.logic.viewmodels.note.NotesViewModel
import com.yeonkims.realnoteapp.util.extension_functions.safeNavigate
import com.yeonkims.realnoteapp.view.fragments.NoteListFragmentDirections

class NoteListAdapter(private val noteViewModel: NotesViewModel): ListAdapter<Note, ViewHolder>(NoteDiffUtil) {

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
        binding.titleVisibility = if(note.title.isBlank()) View.GONE else View.VISIBLE
        binding.contentVisibility = if(note.content.isBlank()) View.GONE else View.VISIBLE

        binding.noteArea.setOnClickListener {
            val action =
            NoteListFragmentDirections.actionNoteListFragmentToSelectedNoteFragment(note)
                it.findNavController().safeNavigate(action)
        }
    }
}

object NoteDiffUtil : DiffUtil.ItemCallback<Note>() {

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.createdDate == newItem.createdDate
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.title == newItem.title && oldItem.content == newItem.content
    }
}