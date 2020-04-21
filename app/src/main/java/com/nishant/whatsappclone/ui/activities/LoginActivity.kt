package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.databinding.ActivityLoginBinding
import com.nishant.whatsappclone.utils.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.apply {
            title = resources.getString(R.string.login)
            setDisplayHomeAsUpEnabled(true)
        }

        auth = FirebaseAuth.getInstance()

        binding.textForgotPassword.setOnClickListener {
            startActivity(ResetPasswordActivity.getIntent(this))
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                toast("All fields are required.")
            else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(
                                MainActivity.getIntent(this).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            )
                            finish()
                        }
                        else
                            toast("Authentication failed!")
                    }
            }
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}
