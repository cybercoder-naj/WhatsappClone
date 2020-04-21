package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.databinding.ActivityResetPasswordBinding
import com.nishant.whatsappclone.utils.toast

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    companion object {
        fun getIntent(context: Context) = Intent(context, ResetPasswordActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_reset_password)

        setSupportActionBar(binding.toolbar.toolbar)
        supportActionBar?.apply {
            title = resources.getString(R.string.reset_password)
            setDisplayHomeAsUpEnabled(true)
        }
        auth = FirebaseAuth.getInstance()

        binding.buttonReset.setOnClickListener {
            val email = binding.editTextResetEmail.text.toString()
            if(email.isBlank())
                toast("All fields are required.")
            else
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            toast("Please check your email.")
                            startActivity(LoginActivity.getIntent(this))
                        }
                        else {
                            toast(it.exception?.message ?: "Error while sending email. Try again later.")
                        }
                    }
        }
    }
}
