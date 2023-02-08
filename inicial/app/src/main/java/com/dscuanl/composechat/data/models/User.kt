package com.dscuanl.composechat.data.models

data class User(
    override var id: String? = "",
    val displayName: String? = "",
    val photoUrl: String? = "",
    val email: String? = "",
) : FirebaseEntity()
