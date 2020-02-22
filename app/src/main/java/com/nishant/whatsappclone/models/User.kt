package com.nishant.whatsappclone.models

data class User(var id: String, var username: String, var imageURL: String) {

    constructor() : this("", "", "")
}