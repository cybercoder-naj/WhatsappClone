package com.nishant.whatsappclone.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.adapters.UserAdapter
import com.nishant.whatsappclone.databinding.FragmentChatsBinding
import com.nishant.whatsappclone.models.Chat
import com.nishant.whatsappclone.models.User

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var binding: FragmentChatsBinding

    companion object {
        const val TAG = "ChatsFragment"
        const val TITLE = "Chats"

        fun newInstance() = ChatsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        binding.recyclerViewChats.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Chats")

        val usersList = ArrayList<String>()
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear()

                for (snapshot in dataSnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)

                    chat?.let {
                        if (it.sender == firebaseUser.uid)
                            usersList.add(it.receiver)
                        if (it.receiver == firebaseUser.uid)
                            usersList.add(it.sender)
                    }

                    readChats(usersList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun readChats(usersList: ArrayList<String>) {
        val users = ArrayList<User>()

        reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()

                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)

                    for(id in usersList) {
                        if (user?.id == id)
                            if (users.size != 0) {
                                for (_user in users) {
                                    if (user.id != _user.id)
                                        users.add(user)
                                }
                            } else {
                                users.add(user)
                            }
                    }
                }

                binding.recyclerViewChats.adapter = UserAdapter(context!!, users)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }
}
