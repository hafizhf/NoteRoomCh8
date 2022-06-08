package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.snackbarShort
import andlima.hafizhfy.noteroomch8.func.toast
import andlima.hafizhfy.noteroomch8.local.datastore.UserManager
import andlima.hafizhfy.noteroomch8.local.room.NoteDatabase
import andlima.hafizhfy.noteroomch8.local.room.notetable.Note
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.asLiveData
import kotlinx.android.synthetic.main.activity_new_note.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class NewNoteActivity : AppCompatActivity() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        userManager = UserManager(this)
        mDb = NoteDatabase.getInstance(this)

        btn_save_note.setOnClickListener {
            val title = et_new_title.text.toString()
            val desc = et_new_desc.text.toString()

            if (title != "") {
                addNote(title, desc)
            } else {
                toast(this, "Write at least a title to save new note")
            }
        }
    }

    private fun addNote(title: String, desc: String) {
        userManager.id.asLiveData().observe(this, { userId ->
            GlobalScope.async {
                val submit = mDb?.noteDao()?.insertNote(Note(null, userId, title, desc))

                runOnUiThread {
                    if (submit != 0.toLong()) {
                        snackbarShort(findViewById(R.id.content), "New note saved")
                        finish()
                    } else {
                        toast(this@NewNoteActivity, "Save failed, try again")
                    }
                }
            }
        })
    }
}