package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.alertDialog
import andlima.hafizhfy.noteroomch8.func.snackbarShort
import andlima.hafizhfy.noteroomch8.func.toast
import andlima.hafizhfy.noteroomch8.local.datastore.UserManager
import andlima.hafizhfy.noteroomch8.local.room.NoteDatabase
import andlima.hafizhfy.noteroomch8.local.room.notetable.Note
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        userManager = UserManager(this)
        mDb = NoteDatabase.getInstance(this)

        val getIntent = intent.getBundleExtra("SELECTED_DATA")
        val selectedData = getIntent?.getParcelable<Note>("DATA") as Note

        tv_title_note.text = selectedData.title
        tv_description_note.text = selectedData.description

        fab_delete_note.setOnClickListener {
            alertDialog(this, "Delete note", "Are you sure want to delete ${selectedData.title}?") {
                deleteNote(selectedData.id!!.toInt())
            }
        }

        fab_edit_note.setOnClickListener {
            val editNote = Intent(this, NewNoteActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("DATA", selectedData)
            editNote.putExtra("SELECTED_DATA", bundle)
            startActivity(editNote)
            overridePendingTransition(0,0)
        }
    }

    private fun deleteNote(noteId: Int) {
        GlobalScope.launch {
            val delete = mDb?.noteDao()?.deleteNote(noteId)

            if (delete != 0) {
                snackbarShort(window.decorView.rootView, "Note deleted")
                finish()
            } else {
                toast(this@DetailActivity, "Delete failed, try again")
            }
        }
    }
}