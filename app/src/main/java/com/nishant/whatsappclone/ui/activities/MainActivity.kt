package com.nishant.whatsappclone.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.adapters.ViewPagerAdapter
import com.nishant.whatsappclone.databinding.ActivityMainBinding
import com.nishant.whatsappclone.models.User
import com.nishant.whatsappclone.ui.fragments.ChatsFragment
import com.nishant.whatsappclone.ui.fragments.ProfileFragment
import com.nishant.whatsappclone.ui.fragments.UsersFragment

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

    override fun onResume() {
        super.onResume()
        status("online")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = ""
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    binding.toolbarUsername.text = it.username
                    if (it.imageURL == "default")
                        binding.imageProfile.setImageResource(R.drawable.default_profile)
                    else
                        Glide.with(this@MainActivity).load(it.imageURL).into(binding.imageProfile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager).apply {
            addFragment(ChatsFragment.newInstance(), ChatsFragment.TITLE)
            addFragment(UsersFragment.newInstance(), UsersFragment.TITLE)
            addFragment(ProfileFragment.newInstance(), ProfileFragment.TITLE)
        }

        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(StartActivity.getIntent(this).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                return true
            }
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        status("offline")
    }

    private fun status(status: String) {
        FirebaseDatabase
            .getInstance()
            .getReference("Users")
            .child(firebaseUser.uid)
            .updateChildren(
                mapOf(
                    "status" to status
                )
            )
    }
}