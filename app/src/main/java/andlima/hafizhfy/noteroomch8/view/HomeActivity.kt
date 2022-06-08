package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.alertDialog
import andlima.hafizhfy.noteroomch8.func.toast
import andlima.hafizhfy.noteroomch8.local.datastore.UserManager
import andlima.hafizhfy.noteroomch8.local.room.NoteDatabase
import andlima.hafizhfy.noteroomch8.view.adapter.AdapterNote
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userManager = UserManager(this)
        mDb = NoteDatabase.getInstance(this)

        userManager.username.asLiveData().observe(this, { username ->
            tv_greeting.append(username)
        })

        fab_add_new_article.setOnClickListener {
            startActivity(Intent(this, NewNoteActivity::class.java))
            overridePendingTransition(0,0)
        }

        btn_logout.setOnClickListener {
            alertDialog(this, "Logout", "Are you sure want to log out?") {
                GlobalScope.launch {
                    userManager.clearData()
                }
                toast(this@HomeActivity, "You are logged out")

                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                overridePendingTransition(0,0)
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getNoteData()
    }

    override fun onDestroy() {
        super.onDestroy()
        NoteDatabase.destroyInstance()
    }

    private fun getNoteData() {
        rv_note_list.layoutManager = LinearLayoutManager(this)

        userManager.id.asLiveData().observe(this, { userId ->
            GlobalScope.launch {
                val listData = mDb?.noteDao()?.getUserNotes(userId.toInt())

                if (listData?.size!! > 0) {
                    runOnUiThread {
                        nothing_handler.visibility = View.GONE
                        listData.let {
                            val adapter = AdapterNote(it) { selectedData ->
                                val noteDetail = Intent(this@HomeActivity, DetailActivity::class.java)
                                val bundle = Bundle()
                                bundle.putParcelable("DATA", selectedData)
                                noteDetail.putExtra("SELECTED_DATA", bundle)
                                startActivity(noteDetail)
                                overridePendingTransition(0,0)
                            }
                            rv_note_list.adapter = adapter
                        }

                    }
                } else {
                    nothing_handler.visibility = View.VISIBLE
                }
            }
        })
    }
}