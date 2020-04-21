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
import com.nishant.whatsappclone.models.Chatlist
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

        val usersList = ArrayList<Chatlist>()

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usersList.clear()
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(Chatlist::class.java)?.let {
                        usersList.add(it)
                    }
                }
                chatlist(usersList)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }

    private fun chatlist(usersList: ArrayList<Chatlist>) {
        val users = ArrayList<User>()
        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    users.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)
                        user?.let {
                            for(chatlist in usersList) {
                                if (it.id == chatlist.id)
                                    users.add(it)
                            }
                        }
                    }
                    binding.recyclerViewChats.adapter = UserAdapter(context!!, users, true)
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }
}
