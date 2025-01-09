package com.shreya.eventorganizer.models

data class Invitation(
    val eventId: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val status: String = "pending"
)
