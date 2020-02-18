package com.nishant.whatsappclone.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_action_bar.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.login)
            setDisplayHomeAsUpEnabled(true)
        }

        auth = FirebaseAuth.getInstance()

        button_login.setOnClickListener {
            val email = edit_text_login_email.text.toString()
            val password = edit_text_login_password.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                toast("All fields are required.")
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = MainActivity.getIntent(this)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                        else
                            toast("Authentication failed!")
                    }
            }
        }
    }
}
