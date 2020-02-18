package com.example.myapplication.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class ReceivedMessageAdapterDelegate :
    AbsListItemAdapterDelegate<Message, Message, ReceivedMessageAdapterDelegate.ReceivedMessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ReceivedMessageViewHolder {
               val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.received_message_item, parent, false)

        return ReceivedMessageViewHolder(itemView)
    }

    override fun isForViewType(item: Message, items: MutableList<Message>, position: Int): Boolean = item.sender == FirebaseAuth.getInstance().currentUser?.uid

    override fun onBindViewHolder(
        item: Message,
        holder: ReceivedMessageViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.messageTextView?.text = item.message
    }

     class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTextView: TextView? = null

        init {
            messageTextView = itemView.findViewById(R.id.messageReceivedTextView)
        }
    }
}