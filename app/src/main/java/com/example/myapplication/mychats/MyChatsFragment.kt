package com.example.myapplication.mychats

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.MychatsFragmentBinding
import com.example.myapplication.models.Chat
import kotlinx.android.synthetic.main.chat_fragment.*
import javax.inject.Inject

class MyChatsFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: MyChatsViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(MyChatsViewModel::class.java)
    }

    val chatAdapter = MyChatsAdapter()

    private lateinit var binding: MychatsFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.mychats_fragment, container,false)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.bind(this)

        setUpChatList()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setActionBar(toolbar)
        activity?.actionBar?.displayOptions
    }

    private fun setUpChatList() {
        binding.chatListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.chatListRecycler.adapter = chatAdapter.apply {
            itemClickListener = object : MyChatsAdapter.OnChatItemClickListener {
                override fun onChatItemClicked(item: Chat) {
                    viewModel.onChatItemClicked(item)
                }
            }
            viewModel.chats.observe(viewLifecycleOwner, Observer {
                it?.let{ this.replaceItems(it)}
            })
        }
    }

    companion object {
        fun newInstance() = MyChatsFragment()
    }
}