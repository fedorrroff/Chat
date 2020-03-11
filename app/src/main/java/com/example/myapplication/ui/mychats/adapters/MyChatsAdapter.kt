package com.example.myapplication.ui.mychats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Chat
import com.example.myapplication.utils.AvatarCreator
import com.example.myapplication.utils.DateUtils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class MyChatsAdapter(
    private val itemList: MutableList<Chat> = mutableListOf()
): RecyclerView.Adapter<MyChatsAdapter.MyChatsViewHolder>() {

    var itemClickListener: OnChatItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChatsViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.mychats_item, parent, false)

        return MyChatsViewHolder(itemView)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: MyChatsViewHolder, position: Int) {
        val chatItem = itemList[position]
        itemClickListener?.let {
            holder.itemView.setOnClickListener {
                itemClickListener!!.onChatItemClicked(chatItem)
            }
        }

        val senderTag = chatItem.messages.lastOrNull()?.senderId
        val interlocutorName = chatItem.users.firstOrNull { it.id != FirebaseAuth.getInstance().currentUser?.uid }?.id

        val interlocutor = chatItem.users.firstOrNull { it.id != FirebaseAuth.getInstance().currentUser?.uid }
        val holderName = interlocutor?.name + " " + (interlocutor?.lastName ?: "")
        val initials = interlocutor?.name?.firstOrNull().toString() + (interlocutor?.lastName!!.firstOrNull() ?: "")

        holder.name?.text = holderName

        val sender = if (interlocutorName != senderTag) {
            "Me"
        } else {
            holderName
        }

        holder.message?.text = "${sender} : ${chatItem.messages.lastOrNull()?.message}"

        holder.avatar?.setImageBitmap(AvatarCreator.generateCircleBitmap(
            holder.avatar!!.context,
            56f,
            initials.toUpperCase())
        )

        val time = DateUtils.toDate(chatItem.messages.lastOrNull()!!.timestamp)
        holder.time?.text = time
    }

    fun replaceItems(items: MutableList<Chat>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun setItems(items: MutableList<Chat>) {
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun add(list: List<Chat>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    fun remove(item: Chat) {
        val pos = itemList.indexOf(item)
        if (pos >= 0) {
            itemList.remove(item)
            notifyItemRemoved(pos)
        }
    }

    fun update(item: Chat){
        val pos = itemList.indexOfFirst { item.chatId == it.chatId }
        if (pos >= 0) {
            itemList[pos] = item
            notifyItemChanged(pos)
        }
    }

    fun pushChatToTop(chatId: String?) {
        val chatIndex = itemList.indexOfFirst { item ->
            item.chatId == chatId
        }

        if(chatIndex != -1 && chatIndex != 0) {
            val chat= itemList[chatIndex]
            itemList.removeAt(chatIndex)
            notifyItemRemoved(chatIndex)
            itemList.add(0, chat)
            notifyItemInserted(0)
        }
    }

    inner class MyChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView? = null
        var message: TextView? = null
        var avatar: ImageView? = null
        var time: TextView? = null

        init {
            name = itemView.findViewById(R.id.nameMySchats)
            message = itemView.findViewById(R.id.messageMyChats)
            avatar = itemView.findViewById(R.id.avatarIvMyChats)
            time = itemView.findViewById(R.id.timeTvMyChats)
        }
    }

    interface OnChatItemClickListener {
        fun onChatItemClicked(item: Chat)
    }
}