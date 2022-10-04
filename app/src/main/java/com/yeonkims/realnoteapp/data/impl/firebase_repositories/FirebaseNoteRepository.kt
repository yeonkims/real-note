package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.util.helpers.toMap
import java.util.*
import javax.inject.Inject


class FirebaseNoteRepository @Inject constructor(
    private val functions: FirebaseFunctions,
    private val gson: Gson,
): NoteRepository {

    private var savedNotesLiveData = MutableLiveData<List<Note>>(null)

    override suspend fun fetchNotes(user: User) {
        functions.getHttpsCallable("getNotes")
            .call(mapOf("user_id" to user.id)).addOnCompleteListener { response ->

            val data = response.result.data
            val listJson = (data as HashMap<*, *>)["res"]

            val dbSavedNotes = gson.fromJson(
                gson.toJson(listJson),
                Array<Note>::class.java
            ).toList()

            savedNotesLiveData.postValue(dbSavedNotes)
        }
    }

    override fun getNotes(): LiveData<List<Note>?> {
        return savedNotesLiveData
    }

    override suspend fun deleteNote(note: Note) {
        val originalSavedNotes = savedNotesLiveData.value!!
        val modifiedSavedNotes = originalSavedNotes.filter { existingNote ->
            return@filter existingNote.id != note.id
        }.toMutableList()
        savedNotesLiveData.value = modifiedSavedNotes

        functions.getHttpsCallable("deleteNote")
            .call(gson.toMap(note)).addOnCompleteListener { response ->
            if(!response.isSuccessful) {
                savedNotesLiveData.value = originalSavedNotes
                throw response.exception!!
            }
        }
    }

    override suspend fun createNote(note: Note) {

        functions.getHttpsCallable("createNote")
            .call(gson.toMap(note))
            .addOnCompleteListener { response ->
                if(response.isSuccessful) {
                    val data = response.result.data
                    val id = (data as HashMap<String, Int>)["ref"]

                    val createdNote = note.copy(id = id)

                    val originalSavedNotes = savedNotesLiveData.value!!
                    val updatedSaveNotes = originalSavedNotes.toMutableList()
                    updatedSaveNotes.add(createdNote)

                    savedNotesLiveData.value = updatedSaveNotes
                } else {
                    throw response.exception!!
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun updateNote(note: Note) {

        val originalSavedNotes = savedNotesLiveData.value!!
        val editingNote = originalSavedNotes.first { existingNote ->
            existingNote.id == note.id
        }
        val editedNote = editingNote.copy(title = note.title, content = note.content)

        functions.getHttpsCallable("updateNote")
            .call(gson.toMap(note))
            .addOnCompleteListener { response ->
                if(response.isSuccessful) {
                    val updatedList = originalSavedNotes.toMutableList()
                    updatedList.replaceAll { note ->
                        if(note.id == editedNote.id) {
                            return@replaceAll editedNote
                        } else {
                            return@replaceAll note
                        }
                    }
                    savedNotesLiveData.value = updatedList
                } else {
                    response.exception!!
                }

            }
    }

    override fun clearNotes() {
        savedNotesLiveData.value = listOf()
    }

}