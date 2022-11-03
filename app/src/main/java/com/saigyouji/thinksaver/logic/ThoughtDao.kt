package com.saigyouji.thinksaver.logic

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ThoughtDao{

    @Query("SELECT * FROM thought")
    fun getAllThoughts(): LiveData<List<Thought>>

    @Query("SELECT * FROM thought WHERE id = (:id)")
    fun getThoughtById(id: Int): LiveData<List<Thought>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg thoughts: Thought)
    @Delete
    suspend fun delete(thought: Thought)
}