package com.example.myapplication.ui.chat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Message
import com.example.myapplication.utils.AvatarCreator
import com.example.myapplication.utils.DateUtils
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

    override fun isForViewType(item: Message, items: MutableList<Message>, position: Int): Boolean = item.senderId == FirebaseAuth.getInstance().currentUser?.uid

    override fun onBindViewHolder(
        item: Message,
        holder: ReceivedMessageViewHolder,
        payloads: MutableList<Any>
    ) {
        holder.messageTextView?.text = item.message
        holder.sender?.text = "Me:"

        holder.messageTimeTextView?.text = DateUtils.toTime(item.timestamp)

        val firstName = item.senderName?.firstOrNull().toString()
        val lastName = item.senderName?.substringAfter(" ", "")
        val initials = if (lastName!!.isNotEmpty()) {
            firstName.plus(lastName.first())
        } else {
            firstName
        }

        holder.myAvatarImageView?.setImageBitmap(
            AvatarCreator.generateCircleBitmap(
                holder.myAvatarImageView!!.context,
                40f,
                initials
            )
        )
    }

     inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var messageTextView: TextView? = null
        var sender: TextView? = null
        var messageTimeTextView: TextView? = null
        var myAvatarImageView: ImageView? = null

        init {
            messageTextView = itemView.findViewById(R.id.messageReceivedTextView)
            sender = itemView.findViewById(R.id.messageSenderMe)
            messageTimeTextView = itemView.findViewById(R.id.timeSentMyTV)
            myAvatarImageView = itemView.findViewById(R.id.avatarMyIvChat)
        }
    }
}