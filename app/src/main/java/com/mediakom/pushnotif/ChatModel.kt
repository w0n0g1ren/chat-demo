package com.mediakom.pushnotif

data class ChatRoomConfigModel(
    var firstUserId: String = "",
    var secondUserId: String = "",
    var chatItem: List<ChatModel> = listOf()
)

data class ChatModel(
var senderId: String = "",
var timestamp: Long? = null,
var text: String = ""
)