package com.nishant.whatsappclone.models

data class User(
    var id: String,
    var username: String,
    var imageURL: String,
    var status: String,
    var search: String
) {

    constructor() : this("", "", "", "", "")
}