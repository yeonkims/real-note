package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import android.os.Build
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createNote(title: String, content: String) {

        functions.getHttpsCallable("createNote").call(mapOf("title" to title, "content" to content))
            .addOnCompleteListener { response ->
                if(response.isSuccessful) {
                    // 1. Get newId
                    val data = response.result.data
                    val id = (data as HashMap<String, Int>)["ref"]

                    // 2. Create new note
                    val createdNote = Note(id!!, title, content, Date())

                    // 3. Add new note to existing list of notes
                    val originalSavedNotes = savedNotesLiveData.value!!
                    val updatedSaveNotes = originalSavedNotes.toMutableList()
                    updatedSaveNotes.add(createdNote)

                    // 4. update list of notes with the new list (original list + new note)
                    savedNotesLiveData.value = updatedSaveNotes
                } else {
                    throw response.exception!!
                }
            }
    }
}