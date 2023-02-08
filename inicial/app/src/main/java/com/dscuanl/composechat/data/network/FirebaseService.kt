package com.dscuanl.composechat.data.network

import com.google.firebase.database.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

fun DatabaseReference.addValueEventListenerFlow(): Flow<DataSnapshot> =
    callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                trySend(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                cancel()
            }
        }
        addValueEventListener(listener)

        awaitClose { removeEventListener(listener) }
    }




