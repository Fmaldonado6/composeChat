package com.dscuanl.composechat.data.network

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class FirebaseService<T>(
    private val dbName: String
) {
    protected val _elements = MutableStateFlow<List<T>>(mutableListOf())
    val elements = _elements.asStateFlow()
    protected val database = Firebase.database

    init {
        register()
    }

    private fun register() {
        this.database.getReference(dbName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    this.onDataChange(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    this.onCancelled(error)
                }

            })
    }

    protected fun onDataChanged(snapshot: DataSnapshot) {
        val elements = snapshot.getValue<GenericTypeIndicator<Map<String,String>>>()
    }

    protected fun onCancelled(error: DatabaseError) {

    }


}