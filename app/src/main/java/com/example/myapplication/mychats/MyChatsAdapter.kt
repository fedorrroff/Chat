package com.example.myapplication.mychats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.Chat
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
        holder.name?.text = chatItem.users.first { it != FirebaseAuth.getInstance().currentUser?.uid }
        holder.message?.text = chatItem.messages.last().message
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

    fun update(item: Chat) {
        val pos = itemList.indexOf(item)
        if (pos >= 0) {
            notifyItemChanged(pos)
        }
    }

    inner class MyChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var name: TextView? = null
        var message: TextView? = null

        init {
            name = itemView.findViewById<TextView>(R.id.nameMySchats)
            message = itemView.findViewById<TextView>(R.id.messageMyChats)
        }
    }

    interface OnChatItemClickListener {
        fun onChatItemClicked(item: Chat)
    }
}