package com.nishant.whatsappclone.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.nishant.whatsappclone.R
import com.nishant.whatsappclone.databinding.FragmentProfileBinding
import com.nishant.whatsappclone.models.User
import com.nishant.whatsappclone.utils.toast

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        const val TAG = "ProfileFragment"
        const val TITLE = "Profile"
        private const val IMAGE_REQUEST = 1

        fun newInstance() = ProfileFragment()
    }

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var binding: FragmentProfileBinding
    private var imageUri: Uri? = null
    private var uploadTask: UploadTask? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
        storageReference = FirebaseStorage.getInstance().getReference("uploads")
        binding = FragmentProfileBinding.inflate(layoutInflater)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                user?.let {
                    binding.username.text = it.username
                    if (it.imageURL == "default")
                        binding.imageProfile.setImageResource(R.drawable.default_profile)
                    else
                        Glide.with(context!!).load(it.imageURL).into(binding.imageProfile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        binding.imageProfile.setOnClickListener {
            openImage()
        }
    }

    private fun openImage() {
        startActivityForResult(
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            },
            IMAGE_REQUEST
        )
    }

    private fun getFileExtension(uri: Uri): String {
        val contentResolver = context?.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return contentResolver?.let {
            mimeTypeMap.getExtensionFromMimeType(it.getType(uri))
        } ?: ""
    }

    private fun uploadImage() {
        if (imageUri != null) {
            val fileReference =
                storageReference.child("${System.currentTimeMillis()}.${getFileExtension(imageUri!!)}")

            uploadTask = fileReference.putFile(imageUri!!)
            uploadTask!!.continueWithTask { task ->
                if (!task.isSuccessful)
                    throw task.exception!!

                fileReference.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val downloadUri = it.result
                    val mUri = downloadUri.toString()

                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)
                        .apply {
                            updateChildren(hashMapOf("imageURL" to mUri) as Map<String, Any>)
                        }
                } else
                    context?.toast("Failed!")
            }.addOnFailureListener {
                context?.toast(it.message!!)
            }
        } else
            context?.toast("No Image Selected!")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            imageUri = data.data
            if (uploadTask != null)
                context?.toast("Upload in progress${'\u8230'}")
            else
                uploadImage()
        }
    }
}