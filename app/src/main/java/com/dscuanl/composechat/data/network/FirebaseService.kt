package com.dscuanl.composechat.data.network

import com.dscuanl.composechat.data.models.FirebaseEntity
import com.dscuanl.composechat.data.models.Message
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
    protected val dbName: String,
    protected val valueType: KClass<T>,
) {
    protected val database = Firebase.database


    open fun add(element: T) {
        val ref = database.getReference(dbName).push()
        element.id = ref.key
        ref.setValue(element)
    }

    open suspend fun get(id: String): T? {
        val ref = database.getReference(dbName).child(id)
        return ref.get().await().getValue(valueType.java)
    }

    open  fun getAsFlow(id: String): Flow<T?> {
        val ref = database.getReference(dbName).child(id)
        return ref.addValueEventListenerFlow().map { it.getValue(valueType.java) }
    }

    open fun getAll(): Flow<List<T?>> {
        val ref = database.getReference(dbName)
        return ref.addValueEventListenerFlow().map {
            return@map it.children.map { value ->
                value.getValue(valueType.java)
            }

        }
    }

    open fun delete(id: String) {
        val ref = database.getReference(dbName).child(id)
        ref.removeValue()
    }

    open fun update(id: String, element: T) {
        val ref = database.getReference(dbName).child(id)
        ref.setValue(element)
    }

}

object UsersService : FirebaseService<User>("users", User::class){
    override fun add(element: User) {
        val ref = database.getReference(dbName).child(element.id!!)
        ref.setValue(element)
    }
}
object MessageService : FirebaseService<Message>("messages", Message::class)


