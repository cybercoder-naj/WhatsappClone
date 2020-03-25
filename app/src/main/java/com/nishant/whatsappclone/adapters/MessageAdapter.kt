package com.nishant.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.databinding.CardviewChatItemRightBinding
import com.nishant.whatsappclone.models.Chat

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
            val binding = CardviewChatItemRightBinding.bind(itemView)
            binding.showMessage.text = chat.message

            if (imageURL == "default")
                binding.imageProfile.setImageResource(R.drawable.default_profile)
            else
                Glide.with(context).load(imageURL).into(binding.imageProfile)

            if (adapterPosition == chats.size - 1)
                binding.textSeen.text =
                    context.resources.getString(if (chat.hasSeen) R.string.seen else R.string.delivered)
            else
                binding.textSeen.visibility = View.GONE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MessageViewHolder(
            LayoutInflater.from(context).inflate(
                if (viewType == MESSAGE_TYPE_RIGHT)
                    R.layout.cardview_chat_item_right
                else
                    R.layout.cardview_chat_item_left,
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