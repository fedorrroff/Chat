package com.example.myapplication.chat

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.chat.adapters.ChatAdapter
import com.example.myapplication.chat.adapters.MarginItemDecoration
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.database.RealtimeDatabase
import com.example.myapplication.databinding.ChatFragmentBinding
import com.example.myapplication.models.Chat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_fragment.*
import javax.inject.Inject

class ChatFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(ChatViewModel::class.java)
    }

    val chatAdapter = ChatAdapter()

    private lateinit var binding: ChatFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.bind(this)

        setUpMessageList()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setActionBar(toolbar)
        activity?.actionBar?.title = "dewwe"

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    private fun setUpMessageList() {
        binding.messageListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.messageListRecycler.adapter = ChatAdapter().apply {
            viewModel.messages.observe(viewLifecycleOwner, Observer {
                it?.let{ this.items = it }
            })
        }
        binding.messageListRecycler.addItemDecoration(MarginItemDecoration(
            resources.getDimension(R.dimen.default_padding).toInt()
        ))
    }

    fun getAuthorizedUser() = FirebaseAuth.getInstance().currentUser?.uid

    companion object {

        const val TAG = "ChatFragment"

        fun newInstance(item: Chat): ChatFragment {
            val fragment = ChatFragment()
            fragment.arguments = Bundle().apply { putSerializable(TAG, item) }
            return fragment
        }
    }
}