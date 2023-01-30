package com.dscuanl.composechat.data.network

import com.beust.klaxon.Klaxon
import com.beust.klaxon.token.Value
import com.dscuanl.composechat.data.models.FirebaseEntity
import com.dscuanl.composechat.data.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import kotlin.reflect.KClass

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

abstract class FirebaseService<T : FirebaseEntity>(
    private val dbName: String,
    private val valueType: KClass<T>,
) {
    private val database = Firebase.database


    fun add(element: T) {
        val ref = database.getReference(dbName).push()
        element.id = ref.key
        ref.setValue(element)
    }

    suspend fun get(id: String): T? {
        val ref = database.getReference(dbName).child(id)
        return ref.get().await().getValue(valueType.java)
    }

    fun getAsFlow(id: String): Flow<T?> {
        val ref = database.getReference(dbName).child(id)
        return ref.addValueEventListenerFlow().map { it.getValue(valueType.java) }
    }

    fun getAll(): Flow<List<T?>> {
        val ref = database.getReference(dbName)
        return ref.addValueEventListenerFlow().map {
            return@map it.children.map { value ->
                value.getValue(valueType.java)
            }

        }
    }

    fun delete(id: String) {
        val ref = database.getReference(dbName).child(id)
        ref.removeValue()
    }

    fun update(id: String, element: T) {
        val ref = database.getReference(dbName).child(id)
        ref.setValue(element)
    }

}

object UsersService : FirebaseService<User>("users", User::class)

