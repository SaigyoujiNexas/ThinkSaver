package com.saigyouji.thinksaver.logic

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Thought::class], version = 1, exportSchema = false)
abstract class ThoughtDatabase(): RoomDatabase() {
    abstract fun thoughtDao() : ThoughtDao
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