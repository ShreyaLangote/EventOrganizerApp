package com.shreya.eventorganizer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val eventList: MutableList<Event>,
    private val onDeleteEvent: (Event) -> Unit  // Explicitly declare that the lambda accepts an Event object
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val eventLocation: TextView = itemView.findViewById(R.id.event_location)
        val eventDate: TextView = itemView.findViewById(R.id.event_date)
        val eventDescription: TextView = itemView.findViewById(R.id.event_description)
        val deleteButton: Button = itemView.findViewById(R.id.delete_event_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.eventName.text = event.name
        holder.eventLocation.text = event.location
        holder.eventDate.text = event.date
        holder.eventDescription.text = event.description

        holder.deleteButton.setOnClickListener {
            onDeleteEvent(event)  // Pass the whole Event object to the callback
        }
    }

    override fun getItemCount(): Int = eventList.size
}
