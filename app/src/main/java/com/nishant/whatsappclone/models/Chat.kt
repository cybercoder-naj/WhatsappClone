package com.nishant.whatsappclone.models

data class Chat(var sender: String, var receiver: String, var message: String, var hasSeen: Boolean) {

    constructor() : this("", "", "", false)
}