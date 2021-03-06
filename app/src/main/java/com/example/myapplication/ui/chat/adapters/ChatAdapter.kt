package com.example.myapplication.ui.chat.adapters

import android.view.View
import com.example.myapplication.R
import com.example.myapplication.models.Message
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class ChatAdapter : ListDelegationAdapter<MutableList<Message>> {

    constructor() : super()
    constructor(delegatesManager: AdapterDelegatesManager<MutableList<Message>>) : super(delegatesManager)

    init {
        items = mutableListOf()
        delegatesManager.addDelegate(ReceivedMessageAdapterDelegate())
        delegatesManager.fallbackDelegate = FallbackAdapterDelegate()
    }

    override fun getItemCount(): Int = items.size

    override fun setItems(items: MutableList<Message>?) {
        super.setItems(items)
        notifyDataSetChanged()
    }

    fun add(list: List<Message>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
        notifyDataSetChanged()
    }

    fun remove(item: Message) {
        val pos = items.indexOf(item)
        if (pos >= 0) {
            items.remove(item)
            notifyItemRemoved(pos)
        }
    }

    fun update(item: Message) {
        val pos = items.indexOf(item)
        if (pos >= 0) {
            notifyItemChanged(pos)
        }
    }

    fun getUserUnreadMessagesCount(userId: String): Int =
        items.count {
            it.senderId != userId && !it.isRead
        }

}