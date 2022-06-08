package andlima.hafizhfy.noteroomch8.local.room

import andlima.hafizhfy.noteroomch8.local.room.notetable.Note
import andlima.hafizhfy.noteroomch8.local.room.notetable.NoteDao
import andlima.hafizhfy.noteroomch8.local.room.usertable.User
import andlima.hafizhfy.noteroomch8.local.room.usertable.UserDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        Note::class
    ],
    version = 1
)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao

    companion object{
        // Instance --------------------------------------------------------------------------------
        private var INSTANCE : NoteDatabase? = null
        fun getInstance(context : Context):NoteDatabase? {
            if (INSTANCE == null){
                synchronized(NoteDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java,"Note.db").build()
                }
            }
            return INSTANCE
        }

        // Destroy Instance ------------------------------------------------------------------------
        fun destroyInstance(){
            INSTANCE = null
        }
    }

}