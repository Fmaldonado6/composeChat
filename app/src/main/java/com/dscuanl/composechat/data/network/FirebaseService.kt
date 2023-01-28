package com.dscuanl.composechat.data.network

import com.beust.klaxon.Klaxon
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject

abstract class FirebaseService<T>(
    protected val dbName: String
) {
    protected val _elements = MutableStateFlow<List<T?>>(mutableListOf())
    val elements = _elements.asStateFlow()
    protected val database = Firebase.database


    init {
        register()
    }

    abstract suspend fun add(element: T)
    abstract suspend fun get(id: String): T?
    abstract suspend fun delete(id: String): T?
    abstract suspend fun update(element: T): T?

    private fun register() {
        this.database.getReference(dbName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onDbDataChanged(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    onDbCancelled(error)
                }

            })
    }

    protected abstract fun onDbDataChanged(snapshot: DataSnapshot)
    protected abstract fun onDbCancelled(error: DatabaseError)


}