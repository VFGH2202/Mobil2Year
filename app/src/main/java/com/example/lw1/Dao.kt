package com.example.lw1
import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Insert
    fun insertItem(item: Item)
    @Query("SELECT * FROM NoteItem")
    fun getAllItems(): Flow<List<Item>>
    @Delete
    fun deleteIt(item: Item)
    @Query("SELECT * FROM NoteItem WHERE id = :Usid")
    fun getById(Usid: Int?): Item
    @Update
    fun updateIt(item: Item)
}