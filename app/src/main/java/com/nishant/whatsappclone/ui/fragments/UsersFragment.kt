package com.nishant.whatsappclone.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.adapters.UserAdapter
import com.nishant.whatsappclone.databinding.FragmentUsersBinding
import com.nishant.whatsappclone.models.User

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val users = ArrayList<User>()
    private lateinit var binding: FragmentUsersBinding
    companion object {
        const val TAG = "UsersFragment"
        const val TITLE = "Users"

        fun newInstance() = UsersFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentUsersBinding.inflate(layoutInflater)
        binding.recyclerViewUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        readUsers()
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!

                    if (user.id != firebaseUser.uid)
                        users.add(user)

                    binding.recyclerViewUsers.adapter = UserAdapter(context!!, users, false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }
}
