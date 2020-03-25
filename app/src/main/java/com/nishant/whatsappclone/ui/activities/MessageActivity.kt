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
import com.nishant.whatsappclone.databinding.ActivityMessageBinding
import com.nishant.whatsappclone.models.Chat
import com.nishant.whatsappclone.models.User
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var binding: ActivityMessageBinding
    private lateinit var seenListener: ValueEventListener

    companion object {
        fun getIntent(context: Context) = Intent(context, MessageActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            startActivity(
                MainActivity.getIntent(this)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }

        binding.recyclerViewMessages.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val userId = intent?.getStringExtra(resources.getString(R.string.intent_key_userid)) ?: ""
        reference = FirebaseDatabase.getInstance().getReference("Users")
            .child(userId)

        binding.btnSend.setOnClickListener {
            val message = binding.textSend.text.toString()
            if (message != "")
                sendMessage(firebaseUser.uid, userId, message)
            binding.textSend.setText("")
        }

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    binding.toolbarUsername.text = it.username
                    if (it.imageURL == "default")
                        binding.imageProfile.setImageResource(R.drawable.default_profile)
                    else
                        Glide.with(applicationContext).load(it.imageURL).into(binding.imageProfile)
                }

                readMessage(firebaseUser.uid, userId, user?.imageURL ?: "default")
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
        seenMessage(userId)
    }

    override fun onPause() {
        super.onPause()
        reference.removeEventListener(seenListener)
        status("offline")
    }

    private fun seenMessage(userId: String) {
        reference = FirebaseDatabase.getInstance().getReference("Chats")
        seenListener = reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    chat?.let {
                        if (chat.receiver == firebaseUser.uid && chat.sender == userId)
                            snapshot.ref.updateChildren(
                                mapOf(
                                    "hasSeen" to true
                                )
                            )
                    }
                }
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
            "message" to message,
            "hasSeen" to false
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

                    binding.recyclerViewMessages.adapter =
                        MessageAdapter(this@MessageActivity, chats, imageURL)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun status(status: String) {
        FirebaseDatabase
            .getInstance()
            .getReference("Users")
            .updateChildren(
                mapOf(
                    "status" to status
                )
            )
    }
}