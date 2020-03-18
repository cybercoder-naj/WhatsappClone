package com.nishant.whatsappclone.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import java.util.*
import kotlin.collections.ArrayList

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

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchUsers(s?.toString()?.toLowerCase(Locale.getDefault()) ?: "")
            }
        })

        readUsers()
    }

    private fun searchUsers(s: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
            .startAt(s)
            .endAt("$s${'\uf8ff'}")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                users.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    if (user.id != firebaseUser.uid)
                        users.add(user)
                }

                binding.recyclerViewUsers.adapter = UserAdapter(context!!, users, false)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        val reference = FirebaseDatabase.getInstance().getReference("Users")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (TextUtils.isEmpty(binding.editTextSearch.text.toString())) {
                    users.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user = snapshot.getValue(User::class.java)!!
                        if (user.id != firebaseUser.uid)
                            users.add(user)
                        binding.recyclerViewUsers.adapter = UserAdapter(context!!, users, false)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })
    }
}
