package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nishant.whatsappclone.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    private var firebaseUser: FirebaseUser? = null

    companion object {
        fun getIntent(context: Context) = Intent(context, StartActivity::class.java)
    }

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null) {
            startActivity(MainActivity.getIntent(this))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        button_start_login.setOnClickListener {
            startActivity(
                LoginActivity.getIntent(this)
            )
        }

        button_start_register.setOnClickListener {
            startActivity(
                RegisterActivity.getIntent(this)
            )
        }
    }
}
