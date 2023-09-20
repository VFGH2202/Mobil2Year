package com.example.lw1
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertItem(item: Item)

    @Query("SELECT * FROM NoteItem")
    fun getAllItems(): Flow<List<Item>>
}