package andlima.hafizhfy.noteroomch8.local.room.notetable

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    @Insert
    fun insertNote(note: Note): Long

    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM Note WHERE ownerId = :ownerId ORDER BY id DESC")
    fun getUserNotes(ownerId: Int): List<Note>

    @Query("DELETE FROM Note WHERE id = :id")
    fun deleteNote(id: Int): Int

    @Query("UPDATE Note SET title = :title AND description = :description WHERE id = :articleId")
    fun updateNote(articleId: Int, title: String, description: String): Int
}