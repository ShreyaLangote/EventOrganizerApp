package com.shreya.eventorganizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class GuestAdapter(
    private val guestList: MutableList<Guest>,
    private val eventId: String
) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    private val selectedGuests = mutableSetOf<String>() // Store selected guests' IDs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_guest, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val guest = guestList[position]
        holder.nameTextView.text = guest.name
        holder.emailTextView.text = guest.email
        holder.rsvpStatusTextView.text = guest.rsvpStatus

        // Handle checkbox for guest selection
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedGuests.add(guest.id)
            } else {
                selectedGuests.remove(guest.id)
            }
        }

        // Handle delete button click
        holder.deleteButton.setOnClickListener {
            // Remove guest from Firebase
            deleteGuest(guest.id)
        }

        // Handle edit button click (You can create an EditGuestDialog)
        holder.editButton.setOnClickListener {
            // Open edit guest dialog and pass the guest data to the dialog
        }

        // Handle RSVP status update
        holder.updateRsvpButton.setOnClickListener {
            updateRsvpStatus(guest.id)
        }
    }

    override fun getItemCount(): Int = guestList.size

    // Update the guest list in the adapter
    fun updateGuestList(newGuestList: List<Guest>) {
        guestList.clear()
        guestList.addAll(newGuestList)
        notifyDataSetChanged()
    }

    // Get the set of selected guests
    fun getSelectedGuests(): Set<String> = selectedGuests

    // Delete guest from Firebase
    private fun deleteGuest(guestId: String) {
        FirebaseDatabase.getInstance().getReference("events")
            .child(eventId).child("guests").child(guestId).removeValue()
    }

    // Update RSVP status for a guest
    private fun updateRsvpStatus(guestId: String) {
        val guestRef = FirebaseDatabase.getInstance().getReference("events")
            .child(eventId).child("guests").child(guestId)

        // Toggle RSVP status (example: Pending -> Accepted, Accepted -> Declined, etc.)
        guestRef.child("rsvpStatus").get().addOnSuccessListener { snapshot ->
            val currentStatus = snapshot.getValue(String::class.java) ?: "Pending"
            val newStatus = when (currentStatus) {
                "Pending" -> "Accepted"
                "Accepted" -> "Declined"
                else -> "Pending"
            }
            guestRef.child("rsvpStatus").setValue(newStatus)
        }
    }

    inner class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.guest_name)
        val emailTextView: TextView = itemView.findViewById(R.id.guest_email)
        val rsvpStatusTextView: TextView = itemView.findViewById(R.id.rsvp_status)
        val checkBox: CheckBox = itemView.findViewById(R.id.guest_checkbox)
        val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        val editButton: Button = itemView.findViewById(R.id.edit_button)
        val updateRsvpButton: Button = itemView.findViewById(R.id.update_rsvp_button)  // New button to update RSVP status
    }
}
