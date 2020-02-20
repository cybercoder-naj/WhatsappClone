package com.nishant.whatsappclone.ui.fragments

import androidx.fragment.app.Fragment

import com.nishant.whatsappclone.R

class UsersFragment : Fragment(R.layout.fragment_users) {

    companion object {
        const val TAG = "UsersFragment"
        const val TITLE = "Users"

        fun newInstance() = UsersFragment()
    }
}
