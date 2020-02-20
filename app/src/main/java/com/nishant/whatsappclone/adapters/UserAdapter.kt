package com.nishant.whatsappclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.models.User
import kotlinx.android.synthetic.main.card_view_user_item.view.*

class UserAdapter(private val context: Context, private val users: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

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
            if (user.imageUrl == "default")
                itemView.display_photo.setImageResource(R.mipmap.ic_launcher)
            else
                Glide.with(context).load(user.imageUrl).into(itemView.display_photo)


        }
    }
}
