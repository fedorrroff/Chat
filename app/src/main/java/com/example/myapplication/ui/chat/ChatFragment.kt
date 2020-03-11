package com.example.myapplication.ui.chat

import android.os.Bundle
import android.service.media.MediaBrowserService
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.ui.chat.adapters.ChatAdapter
import com.example.myapplication.ui.chat.adapters.MarginItemDecoration
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.ChatFragmentBinding
import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import javax.inject.Inject

class ChatFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(ChatViewModel::class.java)
    }

    val chatAdapter = ChatAdapter()

    var currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var binding: ChatFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
        setUpCurrentChat()
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

        viewModel.navigation = Navigation(requireActivity() as AppCompatActivity)
        setUpMessageList()
        subscribeToEvents()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    
    override fun getToolbarTitle(): CharSequence {
        return arguments?.getString(TITLE).toString()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_signout_singlechat -> viewModel.onSignOutButtonClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    private fun setUpMessageList() {
        binding.messageListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.messageListRecycler.layoutManager
        binding.messageListRecycler.adapter = ChatAdapter().apply {
            viewModel.messages.observe(viewLifecycleOwner, Observer {
                it?.let{
                    this.items = it
                }
            })
        }
        binding.messageListRecycler.addItemDecoration(MarginItemDecoration(
            resources.getDimension(R.dimen.default_padding).toInt()
        ))
    }

    private fun setUpCurrentChat() {
        viewModel.currentChat.value = arguments?.getSerializable(TAG) as Chat
    }

    private fun subscribeToEvents() {
        viewModel.scrollToBottomEvent.observe(viewLifecycleOwner, Observer {event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                binding.messageListRecycler.smoothScrollToPosition(it)
            }
        })
    }

    fun getAuthorizedUser() = FirebaseAuth.getInstance().currentUser?.uid

    companion object {

        const val TAG = "ChatFragment"
        const val TITLE = "title"

        fun newInstance(item: Chat, title: String): ChatFragment {
            val fragment = ChatFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(TAG, item)
                putString(TITLE, title)
            }
            return fragment
        }
    }
}