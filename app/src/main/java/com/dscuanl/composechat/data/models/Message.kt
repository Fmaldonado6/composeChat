package com.dscuanl.composechat.data.models

data class Message(
    override var id: String? = "",
    val message: String? = "",
    val author: String? = "",
    val authorPicture: String? = "",
    val authorId: String? = ""
) : FirebaseEntity()
