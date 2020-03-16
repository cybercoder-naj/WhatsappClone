package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.databinding.ActivityRegisterBinding
import com.nishant.whatsappclone.utils.toast
import kotlinx.android.synthetic.main.activity_register.toolbar

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.apply {
            title = resources.getString(R.string.login)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)
            )
                toast("All fields are required!")
            else if (password.length < 8)
                toast("Password be must at least 8 characters.")
            else
                register(username, email, password)
        }
    }

    private fun register(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userId = firebaseUser?.uid

                    userId?.let {
                        reference =
                            FirebaseDatabase.getInstance().getReference("Users").child(userId)
                    }

                    val hashMap = hashMapOf(
                        "id" to userId,
                        "username" to username,
                        "imageURL" to "default"
                    )

                    reference.setValue(hashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(
                                MainActivity.getIntent(this).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            )
                            finish()
                        }
                    }
                } else
                    toast("You cannot register with this email and password.")
            }
    }


    companion object {
        fun getIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }
}