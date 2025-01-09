package com.shreya.eventorganizer

data class Message(
    var sender: String = "", // UID or Username
    val message: String = "",
    val timestamp: Long = 0L
)

