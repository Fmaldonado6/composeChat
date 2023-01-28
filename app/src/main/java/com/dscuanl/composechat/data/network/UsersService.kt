package com.dscuanl.composechat.data.network

import com.dscuanl.composechat.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.tasks.await

object UsersService : FirebaseService<User>("users") {
    override suspend fun add(element: User) {
        val ref = database.getReference(dbName).push()
        ref.setValue(element)
    }

    override suspend fun get(id: String): User? {
        val ref = database.getReference(dbName).child(id)
        val result = ref.get().await()
        return result.getValue<User>()
    }

    override suspend fun delete(id: String): User? {
        val ref = database.getReference(dbName).child(id)
        val user = ref.get().await().getValue<User>()
        ref.removeValue().await()
        return user
    }

    override suspend fun update(element: User): User? {
        val ref = database.getReference(dbName).child(element.id)
        ref.setValue(element).await()
        return element
    }

    override fun onDataChanged(snapshot: DataSnapshot) {
        val users = snapshot.getValue<User>()
        
    }

    override fun onCancelled(error: DatabaseError) {
    }
}