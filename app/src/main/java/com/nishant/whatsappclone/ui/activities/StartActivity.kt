package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nishant.whatsappclone.databinding.ActivityStartBinding

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
        val binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartLogin.setOnClickListener {
            startActivity(
                LoginActivity.getIntent(this)
            )
        }

        binding.buttonStartRegister.setOnClickListener {
            startActivity(
                RegisterActivity.getIntent(this)
            )
        }
    }
}
