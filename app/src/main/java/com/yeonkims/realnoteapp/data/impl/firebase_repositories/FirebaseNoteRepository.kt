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
import com.yeonkims.realnoteapp.util.helpers.parseList
import java.text.SimpleDateFormat
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

    override suspend fun deleteNote(id: Int) {
        savedNotes = savedNotes.filter {  note ->
            return@filter note.id != id
        }.toMutableList()
        savedNotesLiveData.value = savedNotes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createNote(title: String, content: String) {
        var nextId = 1
        if(savedNotes.isNotEmpty()) nextId = savedNotes.last().id + 1

        val currentTime = Date(System.currentTimeMillis())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("ko", "KR"))
        val createdDate = dateFormat.format(currentTime)

        savedNotes.add(Note(nextId, title, content, createdDate))
        savedNotesLiveData.value = savedNotes

        Log.i(javaClass.simpleName, "$savedNotes")
    }
}