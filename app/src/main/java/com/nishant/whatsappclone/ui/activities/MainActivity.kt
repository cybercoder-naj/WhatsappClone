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
import com.nishant.whatsappclone.models.User
import com.nishant.whatsappclone.ui.fragments.ChatsFragment
import com.nishant.whatsappclone.ui.fragments.UsersFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    username.text = it.username
                    if (it.imageUrl == "default")
                        image_profile.setImageResource(R.mipmap.ic_launcher)
                    else
                        Glide.with(this@MainActivity).load(it.imageUrl).into(image_profile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }

        })

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager).apply {
            addFragment(ChatsFragment.newInstance(), ChatsFragment.TITLE)
            addFragment(UsersFragment.newInstance(), UsersFragment.TITLE)
        }

        view_pager.adapter = viewPagerAdapter

        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(StartActivity.getIntent(this))
                finish()
                return true
            }
        }
        return false
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}