package com.shreya.eventorganizer

data class Guest(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val invitationStatus: String = "Not Sent",
    val rsvpStatus: String = "Pending"
)

