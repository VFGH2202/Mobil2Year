package com.example.lw1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "NoteItem")
data class Item (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "Heading")
    var head: String = "#",
    @ColumnInfo(name = "Type")
    var type: String = "",
    @ColumnInfo(name = "Data")
    var data: String = "#",
    @ColumnInfo(name = "DateTime")
    var datetime: String
        ) {
}