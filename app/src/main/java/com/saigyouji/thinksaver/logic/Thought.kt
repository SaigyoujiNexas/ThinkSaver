package com.saigyouji.thinksaver.logic

import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Thought(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    var topic: String = "",
    @ColumnInfo
    var content: String = ""
)