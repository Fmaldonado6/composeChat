package com.dscuanl.composechat.data.repositories

import com.dscuanl.composechat.data.network.MessageService

object ChatRepository {

    val messages = MessageService.getAll()

}