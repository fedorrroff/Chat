package com.example.myapplication.ui.chat.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Message
import com.hannesdorfmann.adapterdelegates4.AbsFallbackAdapterDelegate

class FallbackAdapterDelegate(

) : AbsFallbackAdapterDelegate<MutableList<Message>>() {

    override fun onBindViewHolder(
        items: MutableList<Message>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val dumdViewHolder = holder as FallbackViewHolder
        dumdViewHolder.messageTextView?.text = items[position].message
        dumdViewHolder.messageTextView?.setTextColor(Color.BLUE)

        dumdViewHolder.messageSenderTextView?.text = "${items[position].senderName}:"
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.sent_message_item, parent, false)

        return FallbackViewHolder(itemView)
    }

    inner class FallbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTextView: TextView? = null
        var messageSenderTextView: TextView? = null

        init {
            messageTextView = itemView.findViewById(R.id.messageSentTextView)
            messageSenderTextView = itemView.findViewById(R.id.messageSenderItem)
        }
    }
}