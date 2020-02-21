package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.models.User
import kotlinx.android.synthetic.main.activity_main.*

class MessageActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference

    companion object {
        fun getIntent(context: Context) = Intent(context, MessageActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { finish() }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(intent?.getStringExtra(resources.getString(R.string.intent_key_userid)) ?: "")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    toolbar_username.text = it.username
                    if (it.imageUrl == "default")
                        image_profile.setImageResource(R.drawable.ic_launcher_background)
                    else
                        Glide.with(this@MessageActivity).load(it.imageUrl).into(image_profile)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}