package com.nishant.whatsappclone.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nishant.whatsappclone.R
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

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
