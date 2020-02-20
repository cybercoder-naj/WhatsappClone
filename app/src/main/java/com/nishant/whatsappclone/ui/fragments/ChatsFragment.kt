package com.nishant.whatsappclone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.nishant.whatsappclone.R

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    companion object {
        const val TAG = "ChatsFragment"
        const val TITLE = "Chats"

        fun newInstance() = ChatsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }
}
