package com.shreya.eventorganizer

data class Event(
    val id: String = "",  // Optional, automatically assigned by Firebase when we push
    val name: String = "",
    val location: String = "",
    val date: String = "",
    val description: String = "",
    val hostId: String = ""  // User ID of the event host
)
 {
    // Override equals() and hashCode() to ensure correct value comparison
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

