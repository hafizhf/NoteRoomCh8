package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.hideAllPopUp
import andlima.hafizhfy.noteroomch8.func.showPassword
import andlima.hafizhfy.noteroomch8.func.showPopUp
import andlima.hafizhfy.noteroomch8.func.toast
import andlima.hafizhfy.noteroomch8.local.datastore.UserManager
import andlima.hafizhfy.noteroomch8.local.room.NoteDatabase
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // Used for double back to exit app
    private var doubleBackToExit = false

    // Get data store
    lateinit var userManager: UserManager

    private var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        userManager = UserManager(this)
        mDb = NoteDatabase.getInstance(this)

        isUserLoggedIn()

        doubleBackExit()

        btn_show_pwd.setOnClickListener {
            showPassword(et_password, btn_show_pwd)
        }

        btn_goto_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener {
            hideAllPopUp(cv_email_popup, cv_password_popup)

            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email != "" && password != "") {
                loginAuth(email, password)
            } else {
                toast(this, "Please input all field")
            }
        }
    }

    private fun loginAuth(email:String, password: String) {
        GlobalScope.launch {
            val findUser = mDb?.userDao()?.findUser(email)

            when {
                findUser?.size == 0 -> {
                    runOnUiThread {
                        showPopUp(
                            cv_email_popup,
                            tv_email_popup,
                            "Email not registered"
                        )
                    }
                }
                password != findUser!![0].password -> {
                    runOnUiThread {
                        showPopUp(
                            cv_password_popup,
                            tv_password_popup,
                            "Wrong Password"
                        )
                    }
                }
                else -> {
                    login(
                        findUser[0].id!!,
                        findUser[0].username!!,
                        findUser[0].email!!,
                        findUser[0].password!!
                    )
                }
            }
        }
    }

    private fun login(id: Int, username: String, email: String, password: String) {
        GlobalScope.launch {
            userManager.loginUserData(id.toString(), username, email, password)
        }
    }

    private fun isUserLoggedIn() {
        userManager.email.asLiveData().observe(this, { email ->
            userManager.password.asLiveData().observe(this, { password ->

                if (email != "" && password != "") {
                    startActivity(Intent(this, HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                }
            })
        })
    }

    // Function to exit app with double click on back button
    private fun doubleBackExit() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (doubleBackToExit) {
                    finish()
                } else {
                    doubleBackToExit = true
                    Toast.makeText(this@LoginActivity, "Press again to exit", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed(Runnable {
                        kotlin.run {
                            doubleBackToExit = false
                        }
                    }, 2000)
                }
            }
        })
    }
}