package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.snackbarLong
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

        val getIntent = intent.getBundleExtra("SELECTED_DATA")

        if (getIntent == null) {
            btn_save_note.setOnClickListener {
                val title = et_new_title.text.toString()
                val desc = et_new_desc.text.toString()

                if (title != "") {
                    addNote(title, desc)
                } else {
                    toast(this, "Write at least a title to save new note")
                }
            }
        } else {
            val selectedData = getIntent.getParcelable<Note>("DATA") as Note

            title_new_note.text = "Edit note"

            et_new_title.setText(selectedData.title)
            et_new_desc.setText(selectedData.description)

            btn_save_note.setOnClickListener {
                val title = et_new_title.text.toString()
                val desc = et_new_desc.text.toString()

                if (title != "") {
                    editNote(selectedData.id!!.toInt(), title, desc)
                } else {
                    toast(this, "Write at least a title to save new note")
                }
            }
        }

    }

    private fun editNote(noteId: Int, title: String, desc: String) {
        GlobalScope.async {
            val edit = mDb?.noteDao()?.updateNote(noteId, title, desc)

            if (edit != 0) {
                snackbarShort(window.decorView.rootView, "Note edited")
                finish()
            } else {
                toast(this@NewNoteActivity, "Edit failed, try again")
            }
        }
    }

    private fun addNote(title: String, desc: String) {
        userManager.id.asLiveData().observe(this, { userId ->
            GlobalScope.async {
                val submit = mDb?.noteDao()?.insertNote(Note(null, userId, title, desc))

                runOnUiThread {
                    if (submit != 0.toLong()) {
                        snackbarLong(window.decorView.rootView, "New note saved")
                        finish()
                    } else {
                        toast(this@NewNoteActivity, "Save failed, try again")
                    }
                }
            }
        })
    }
}