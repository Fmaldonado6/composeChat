package com.dscuanl.composechat.data.repositories

import com.dscuanl.composechat.data.models.Message
import com.dscuanl.composechat.data.network.MessageService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

object ChatRepository {

    val messages = MessageService.getAll().onEach {
        it.map { message ->
            message?.myMessage = AuthRepository.user.value?.id == message?.authorId
        }
    }


    fun sendMessage(message: Message) {
        MessageService.add(message)
    }

}