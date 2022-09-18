package com.yeonkims.realnoteapp.logic.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.util.livedata.PairLiveData
import com.yeonkims.realnoteapp.util.livedata.combine
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class CreateNoteDialogViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val errorViewModel: ErrorViewModel
    ) : ViewModel() {

    var newTitle: MutableLiveData<String> = MutableLiveData("")
    var newContent: MutableLiveData<String> = MutableLiveData("")

    var combined: PairLiveData<String, String> = newTitle.combine(newContent)

    var createIsEnabled = Transformations.map(combined) { combined ->
        val title = combined.first
        val content = combined.second

        return@map !title.isNullOrEmpty() && !content.isNullOrEmpty()
    }

    fun createNote() {
        viewModelScope.launch {
            try {
                repository.createNote(newTitle.value!!, newContent.value!!)
            } catch (e: Exception) {
                errorViewModel.recordErrorMessage(e.message)
            }
        }
    }

}