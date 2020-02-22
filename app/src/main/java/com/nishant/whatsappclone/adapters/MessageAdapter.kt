package com.nishant.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.models.Chat
import kotlinx.android.synthetic.main.cardview_chat_item_left.view.*

class MessageAdapter(
    private val context: Context,
    private val chats: List<Chat>,
    private val imageURL: String
) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private lateinit var firebaseUser: FirebaseUser

    companion object {
        const val MESSAGE_TYPE_RIGHT = 1
        const val MESSAGE_TYPE_LEFT = 0
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(chat: Chat) {
            val showMessage = itemView.findViewById<TextView>(R.id.show_message)
            val imageProfile = itemView.findViewById<ImageView>(R.id.image_profile)
            showMessage.text = chat.message

            if (imageURL == "default")
                imageProfile.setImageResource(R.drawable.default_profile)
            else
                Glide.with(context).load(imageURL).into(imageProfile)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder(
            LayoutInflater.from(context).inflate(
                if (viewType == MESSAGE_TYPE_RIGHT)
                    R.layout.cardview_chat_item_right
                else R.layout.cardview_chat_item_left,
                parent,
                false
            )
        )

    override fun getItemCount() = chats.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) =
        holder.bindData(chats[position])

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (chats[position].sender == firebaseUser.uid)
            MESSAGE_TYPE_RIGHT
        else
            MESSAGE_TYPE_LEFT
    }
}