package com.shreya.eventorganizer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shreya.eventorganizer.R
import com.shreya.eventorganizer.models.Invitation

class InvitationAdapter(private val invitationsList: MutableList<Invitation>) :
    RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder>() {

    // Create the ViewHolder when the RecyclerView needs to display a new item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.invitation_item, parent, false)
        return InvitationViewHolder(view)
    }

    // Bind the data to the views of each item in the RecyclerView
    override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
        val invitation = invitationsList[position]
        holder.tvMessage.text = invitation.message
        holder.tvTimestamp.text = invitation.timestamp.toString()  // You can format this timestamp to a readable format

        // Accept Button Action
        holder.btnAccept.setOnClickListener {
            // Handle the accept action here (you can update the status in Firebase)
            // Example: updateInvitationStatus(invitation, "accepted")
        }

        // Decline Button Action
        holder.btnDecline.setOnClickListener {
            // Handle the decline action here (you can update the status in Firebase)
            // Example: updateInvitationStatus(invitation, "declined")
        }
    }

    // Return the total count of invitations
    override fun getItemCount(): Int = invitationsList.size

    // Method to update the list of invitations in the adapter
    fun updateInvitations(newInvitations: List<Invitation>) {
        invitationsList.clear()
        invitationsList.addAll(newInvitations)
        notifyDataSetChanged()
    }

    // ViewHolder class that holds the references to the views in each item
    inner class InvitationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)   // Reference to the message TextView
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp) // Reference to the timestamp TextView
        val btnAccept: Button = itemView.findViewById(R.id.btnAccept)  // Reference to the Accept button
        val btnDecline: Button = itemView.findViewById(R.id.btnDecline)  // Reference to the Decline button
    }
}
