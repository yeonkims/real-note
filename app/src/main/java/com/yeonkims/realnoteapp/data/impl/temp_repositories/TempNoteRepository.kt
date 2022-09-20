package com.yeonkims.realnoteapp.data.impl.temp_repositories

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import kotlinx.coroutines.delay
import java.net.HttpRetryException
import java.text.SimpleDateFormat
import java.util.*

class TempNoteRepository : NoteRepository {

    var savedNotes = mutableListOf(Note(1, "마트가면 살 것", "불고기\n소금\n아이스크림", "2022-09-11"),
        Note(2, "귀국 선물 리스트", "- 찻잔\n- 러쉬\n - 쿠키 세트", "2022-09-12"),
        Note(3, "세인이랑 갈 곳", "오늘 발견한 카페랑 케밥집\n세븐 시스터즈","2022-09-13"),
        Note(4, "Today's mood", "peaceful, happy", "2022-09-15"))
    var savedNotesLiveData = MutableLiveData<List<Note>>(null)

    var numberOfErrors = 0

    override suspend fun fetchNotes() {
        delay(1000L)
        if(numberOfErrors > 0){
            numberOfErrors--
            throw HttpRetryException("failed to load", 500)
        }
        savedNotesLiveData.postValue(savedNotes)
    }

    override fun getNotes(): LiveData<List<Note>?> {
        return savedNotesLiveData
    }

    override suspend fun deleteNote(id: Int) {
        //throw HttpRetryException("failed to delete", 500)
        savedNotes = savedNotes.filter {  note ->
            return@filter note.id != id
        }.toMutableList()
        savedNotesLiveData.value = savedNotes
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun createNote(title: String, content: String) {
        //throw HttpRetryException("failed to create", 500)
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