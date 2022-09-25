package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import java.util.*
import javax.inject.Inject


class FirebaseNoteRepository @Inject constructor(
    private val functions: FirebaseFunctions,
    private val gson: Gson,
): NoteRepository {

    private var savedNotes = mutableListOf<Note>()
    private var savedNotesLiveData = MutableLiveData<List<Note>>(null)

    override suspend fun fetchNotes() {
        functions.getHttpsCallable("getNotes").call().addOnCompleteListener { response ->

            val data = response.result.data
            val listJson = (data as HashMap<*, *>)["res"]

            val dbSavedNotes = gson.fromJson(
                gson.toJson(listJson),
                Array<Note>::class.java
            ).toList()

            //val dbSavedNotes = gson.parseList<Note>(response.result)
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

        functions.getHttpsCallable("deleteNote").call(mapOf("id" to note.id)).addOnCompleteListener { response ->
            if(!response.isSuccessful) {
                savedNotesLiveData.value = originalSavedNotes
                throw response.exception!!
            }
        }
    }

    override suspend fun createNote(title: String, content: String) {

        functions.getHttpsCallable("createNote").call(mapOf("title" to title, "content" to content))
            .addOnCompleteListener { response ->
                if(response.isSuccessful) {
                    val data = response.result.data
                    val id = (data as HashMap<String, Int>)["ref"]

                    val createdNote = Note(id!!, title, content, Date(), null)

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
    override suspend fun updateNote(id: Int, title: String, content: String) {

        val originalSavedNotes = savedNotesLiveData.value!!
        val editingNote = originalSavedNotes.first { existingNote ->
            existingNote.id == id
        }
        val editedNote = editingNote.copy(title = title, content = content)

        functions.getHttpsCallable("updateNote").call(mapOf("id" to id, "title" to title, "content" to content))
            .addOnCompleteListener { response ->
                if(response.isSuccessful) {
                    val updatedList = originalSavedNotes.toMutableList()
                    updatedList.replaceAll { note ->
                        if(note.id == id) {
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


}