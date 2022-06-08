package andlima.hafizhfy.noteroomch8.view

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.func.*
import andlima.hafizhfy.noteroomch8.local.room.NoteDatabase
import andlima.hafizhfy.noteroomch8.local.room.usertable.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class RegisterActivity : AppCompatActivity() {

    private var mDb: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mDb = NoteDatabase.getInstance(this)

        // Show password
        btn_show_new_pwd.setOnClickListener {
            showPassword(et_new_password, btn_show_new_pwd)
        }

        // Show re-enter password
        btn_show_new_repwd.setOnClickListener {
            showPassword(et_reenter_password, btn_show_new_repwd)
        }

        btn_goto_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            overridePendingTransition(0,0)
            finish()
        }

        btn_register.setOnClickListener {
            // Get input from EditText
            val username = et_new_name.text.toString()
            val email = et_new_email.text.toString()
            val password = et_new_password.text.toString()
            val repassword = et_reenter_password.text.toString()

            hideInputErrorPopUp()

            if (username != "" && email != "" && password != "" && repassword != "") {
                registerAuth(username, email, password, repassword)
            } else {
                toast(this, "Please input in all field")
            }
        }
    }

    private fun registerAuth(
        username: String,
        email: String,
        password: String,
        repassword: String
    ) {
        GlobalScope.async {
            val registeredEmail = mDb?.userDao()?.findUser(email)

            when {
                registeredEmail?.size!! > 0 -> {
                    runOnUiThread {
                        showPopUp(
                            cv_new_email_popup,
                            tv_new_email_popup,
                            "Email already registered"
                        )
                    }
                }
                repassword != password -> {
                    runOnUiThread {
                        showPopUp(
                            cv_re_pwd_popup,
                            tv_re_pwd_popup,
                            "Password not match"
                        )
                    }
                }
                else -> {
                    register(username, email, password, repassword)
                }
            }
        }
    }

    private fun register(username: String, email: String, password: String, repassword: String) {
        GlobalScope.async {
            val register = mDb?.userDao()?.insertUser(User(null, username, email, password))

            runOnUiThread {
                if (register != 0.toLong()) {
                    snackbarShort(window.decorView.rootView, "Register success")

                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                } else {
                    toast(this@RegisterActivity, "Register error, try again.")
                }
            }
        }
    }

    private fun hideInputErrorPopUp() {
        // Hide every popup
        hidePopUp(cv_name_popup)
        hidePopUp(cv_new_email_popup)
        hidePopUp(cv_re_pwd_popup)
    }
}