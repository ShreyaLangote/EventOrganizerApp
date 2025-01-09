package com.shreya.eventorganizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.util.*

class MessageAdapter(private val messagesList: MutableList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messagesList[position]

        // Display the sender's email (or username if required)
        holder.tvUsername.text = message.sender

        // Display the actual message text
        holder.tvMessage.text = message.message

        // Format the timestamp and display it
        val timestamp = Date(message.timestamp)
        holder.tvTimestamp.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(timestamp)
    }

    override fun getItemCount(): Int = messagesList.size

    // ViewHolder class to represent the message item
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUsername: TextView = itemView.findViewById(R.id.tvUsername) // Sender's email or username
        val tvMessage: TextView = itemView.findViewById(R.id.tvMessage) // Message text
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvTimestamp) // Formatted timestamp
    }

    // Call this method to update the messages in the adapter and refresh the RecyclerView
    fun updateMessages(newMessages: List<Message>) {
        messagesList.clear()
        messagesList.addAll(newMessages)
        notifyDataSetChanged()
    }
}
