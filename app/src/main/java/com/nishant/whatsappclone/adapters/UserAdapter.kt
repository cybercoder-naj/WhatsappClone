package com.nishant.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.models.Chat
import com.nishant.whatsappclone.models.User
import com.nishant.whatsappclone.ui.activities.MessageActivity
import kotlinx.android.synthetic.main.card_view_user_item.view.*

class UserAdapter(
    private val context: Context,
    private val users: List<User>,
    private val isChat: Boolean
) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var mLastMessage: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.card_view_user_item,
                parent,
                false
            )
        )

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bindData(users[position])

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(user: User) {
            itemView.username.text = user.username
            if (user.imageURL == "default")
                itemView.display_photo.setImageResource(R.drawable.default_profile)
            else
                Glide.with(context).load(user.imageURL).into(itemView.display_photo)

            if (isChat)
                lastMessage(user.id, itemView.text_last_message)
            else
                itemView.text_last_message.visibility = View.GONE

            itemView.image_status.visibility =
                if (isChat)
                    if (user.status == "online")
                        View.VISIBLE
                    else
                        View.GONE
                else
                    View.GONE

            itemView.setOnClickListener {
                context.startActivity(
                    MessageActivity.getIntent(context).apply {
                        putExtra(context.resources.getString(R.string.intent_key_userid), user.id)
                    }
                )
            }
        }
    }

    private fun lastMessage(userId: String, lastMessage: TextView) {
        mLastMessage = "default"
        val firebaseUser = FirebaseAuth.getInstance().currentUser!!
        FirebaseDatabase.getInstance().getReference("Chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val chat = snapshot.getValue(Chat::class.java)
                        chat?.let {
                            if ((it.receiver == firebaseUser.uid && it.sender == userId) ||
                                (it.sender == firebaseUser.uid && it.receiver == userId)
                            )
                                mLastMessage = chat.message
                        }
                    }
                    when (mLastMessage) {
                        "default" -> lastMessage.text = ""
                        else -> lastMessage.text = mLastMessage
                    }

                    mLastMessage = "default"
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
    }
}
