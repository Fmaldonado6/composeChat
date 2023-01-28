package com.dscuanl.composechat.data.network

import com.dscuanl.composechat.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue

object UsersService : FirebaseService<User>("users") {
    override fun onDataChanged(snapshot: DataSnapshot) {
        val users = snapshot.getValue<User>()
    }

    override fun onCancelled(error: DatabaseError) {
    }
}