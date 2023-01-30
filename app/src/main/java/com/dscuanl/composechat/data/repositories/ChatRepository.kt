package com.dscuanl.composechat.data.repositories

import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.network.MessageService

object ChatRepository {

    val messages = MessageService.getAll()


    fun sendMessage(message: Message) {
        MessageService.add(message)
    }

}