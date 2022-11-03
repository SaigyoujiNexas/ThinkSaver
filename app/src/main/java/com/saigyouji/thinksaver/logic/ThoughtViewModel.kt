package com.saigyouji.thinksaver.logic

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class ThoughtViewModel(): ViewModel() {

    private lateinit var dao: ThoughtDao
    lateinit var thoughts: LiveData<List<Thought>>

    fun init(context: Context){
        dao = getDatabase(context).thoughtDao()
        thoughts = dao.getAllThoughts()
    }
    fun saveThought(thought: Thought){
        viewModelScope.launch {
            dao.insertAll(thought)
        }
    }
    fun deleteThought(thought: Thought){
        viewModelScope.launch {
            dao.delete(thought)
        }
    }

    private val _state: MutableLiveData<State> = MutableLiveData(State.First)
    val state: LiveData<State>
    get() = _state
    fun openContent(thought: Thought){
        _state.postValue(State.Second(thought))

    }
    fun back(){
        _state.postValue(State.First)
    }
    companion object{
        var db: ThoughtDatabase? = null
        fun getDatabase(context: Context): ThoughtDatabase{
            if(db === null){
                synchronized(this){
                    if(db === null){
                        db = Room.databaseBuilder(context.applicationContext,
                            ThoughtDatabase::class.java, "database-thoughts").build()
                    }
                }
            }
            return db!!
        }
    }
}

sealed interface State{
    object First: State;
    class Second(val thought: Thought): State
}