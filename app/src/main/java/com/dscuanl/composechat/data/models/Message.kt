package com.dscuanl.composechat.data.models

import com.google.firebase.database.Exclude

data class Message(
    override var id: String? = "",
    val message: String? = "",
    val author: String? = "",
    val authorPicture: String? = "",
    val authorId: String? = "",
    @Exclude
    var myMessage: Boolean = false
) : FirebaseEntity()
