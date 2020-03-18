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

        val item = items[position]
        dumdViewHolder.messageTextView?.text = item.message
        dumdViewHolder.messageSenderTextView?.text = "${item.senderName}:"

        val time = DateUtils.toTime(item.timestamp)
        dumdViewHolder.messageTimeTextView?.text = time

        val firstName = item.senderName?.firstOrNull().toString()
        val lastName = item.senderName?.substringAfter(" ", "")
        val initials = if (lastName!!.isNotEmpty()) {
            firstName.plus(lastName.first())
        } else {
            firstName
        }

        dumdViewHolder.avatarImageView?.setImageBitmap(
            AvatarCreator.generateCircleBitmap(
                dumdViewHolder.avatarImageView!!.context,
                40f,
                initials
            )
        )
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
        var messageTimeTextView: TextView? = null
        var avatarImageView: ImageView? = null

        init {
            messageTextView = itemView.findViewById(R.id.messageSentTextView)
            messageSenderTextView = itemView.findViewById(R.id.messageSenderItem)
            messageTimeTextView = itemView.findViewById(R.id.timeSentTV)
            avatarImageView = itemView.findViewById(R.id.avatarIvChat)
        }
    }
}