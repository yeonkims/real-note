package com.yeonkims.realnoteapp.logic.viewmodels.note


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.livedata.PairLiveData
import com.yeonkims.realnoteapp.util.livedata.combine
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class CreateNoteDialogViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val alertViewModel: AlertViewModel
    ) : ViewModel() {

    private val userId
        get() = userRepository.getCurrentUser().value!!.id

    val newTitle: MutableLiveData<String> = MutableLiveData("")
    val newContent: MutableLiveData<String> = MutableLiveData("")

    val combined: PairLiveData<String, String> = newTitle.combine(newContent)

    val createIsEnabled = Transformations.map(combined) { combined ->
        val title = combined.first
        val content = combined.second

        return@map !title.isNullOrEmpty() && !content.isNullOrEmpty()
    }

    fun createNote() {
        viewModelScope.launch {
            try {
                val newNote = Note.newNote(newTitle.value!!, newContent.value!!, userId)
                noteRepository.createNote(newNote)
            } catch (e: Exception) {
                alertViewModel.recordAlertMessage(e.message)
            }
        }
    }

}