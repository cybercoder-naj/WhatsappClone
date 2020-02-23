package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.adapters.MessageAdapter
import com.nishant.whatsappclone.models.Chat
import com.nishant.whatsappclone.models.User
import kotlinx.android.synthetic.main.activity_main.image_profile
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_main.toolbar_username
import kotlinx.android.synthetic.main.activity_message.*

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

        recycler_view_messages.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userId = intent?.getStringExtra(resources.getString(R.string.intent_key_userid)) ?: ""
        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(userId)

        btn_send.setOnClickListener {
            val message = text_send.text.toString()
            if (message != "")
                sendMessage(firebaseUser.uid, userId, message)
            text_send.setText("")
        }

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    toolbar_username.text = it.username
                    if (it.imageURL == "default")
                        image_profile.setImageResource(R.drawable.default_profile)
                    else
                        Glide.with(this@MessageActivity).load(it.imageURL).into(image_profile)
                }

                readMessage(firebaseUser.uid, userId, user?.imageURL ?: "default")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }

    private fun sendMessage(sender: String, receiver: String, message: String) {
        reference = FirebaseDatabase.getInstance().reference

        val hashMap = hashMapOf(
            "sender" to sender,
            "receiver" to receiver,
            "message" to message
        )

        reference.child("Chats").push().setValue(hashMap)
    }

    private fun readMessage(myId: String, userId: String, imageURL: String) {
        val chats = ArrayList<Chat>()

        reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                chats.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    chat?.let {
                        if ((it.receiver == myId && it.sender == userId) ||
                            (it.receiver == userId && it.sender == myId)
                        )
                            chats.add(chat)
                    }

                    recycler_view_messages.adapter =
                        MessageAdapter(this@MessageActivity, chats, imageURL)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}