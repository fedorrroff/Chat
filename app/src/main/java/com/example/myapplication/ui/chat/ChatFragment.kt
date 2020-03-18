package com.example.myapplication.ui.chat

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.chat.adapters.ChatAdapter
import com.example.myapplication.ui.chat.adapters.MarginItemDecoration
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.ChatFragmentBinding
import com.example.myapplication.models.Chat
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.utils.DateUtils
import com.example.myapplication.utils.makeGone
import com.example.myapplication.utils.makeVisible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_fragment.*
import javax.inject.Inject

class ChatFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(ChatViewModel::class.java)
    }

    private lateinit var binding: ChatFragmentBinding

    val layoutManager =  LinearLayoutManager(activity)
    val mAdapter = ChatAdapter()
    var isFirstAttach = true

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_FIRST, isFirstAttach)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null) {
            isFirstAttach = savedInstanceState.getBoolean(IS_FIRST)
        }
    }

    private fun setUpMessageList() {
        binding.messageListRecycler.layoutManager = layoutManager

        binding.messageListRecycler.adapter = mAdapter.apply {
            viewModel.messages.observe(viewLifecycleOwner, Observer {
                it?.let{
                    this.items = it
                    if(isFirstAttach) {
                        val posToScroll = itemCount - getUserUnreadMessagesCount(getAutorizdUser()!!) - 1
                        binding.messageListRecycler.scrollToPosition(posToScroll)
                        isFirstAttach = false
                    }
                }
            })
        }

        binding.messageListRecycler.addItemDecoration(MarginItemDecoration(
            resources.getDimension(R.dimen.default_padding).toInt()
        ))

        binding.messageListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val topMessageTime =  mAdapter.items[layoutManager.findFirstVisibleItemPosition()].timestamp
                val firstVisible = layoutManager.findFirstVisibleItemPosition()
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                //TODO если буду делать удаление,то нужен будет сдвиг в массиве и на серве
                (firstVisible..lastVisible).forEach {
                    if (mAdapter.items[it].senderId != getAutorizdUser() && !mAdapter.items[it].isRead) {
                        mAdapter.items[it].isRead = true
                        viewModel.markMessageAsRead(it)
                    }
                }

                dateTvChat?.text = DateUtils.toDate(topMessageTime)
                if (dy > 0 && fabChat?.visibility == View.VISIBLE) {
                    fabChat?.hide()
                } else if (dy < 0 && fabChat?.visibility != View.VISIBLE) {
                    fabChat?.show()
                }

                if (!binding.messageListRecycler.canScrollVertically(1)) {
                    fabChat?.count = 0
                    fabChat?.hide()
                }
            }
        })
    }

    private fun scrollWhenNewMsgReceivedIfNeeded(): Boolean {
        val userPosition = layoutManager.findLastCompletelyVisibleItemPosition()
        if (userPosition == -1) {
            return false
        }
        if (userPosition == mAdapter.itemCount - 2) {
            binding.messageListRecycler.smoothScrollToPosition(mAdapter.itemCount)
            return true
        } else {
            fabChat?.count = mAdapter.getUserUnreadMessagesCount(getAutorizdUser()!!)
        }
        return false
    }

    private fun setUpCurrentChat() {
        viewModel.currentChat.value = arguments?.getSerializable(TAG) as Chat
    }

    private fun subscribeToEvents() {
        viewModel.scrollToBottomEvent.observe(viewLifecycleOwner, Observer {event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                //Scroll to unread messages or bottom
                var posToScroll = mAdapter.items.indexOfFirst {message ->
                    message.senderId != getAutorizdUser() && !message.isRead
                }

                if (posToScroll == -1) {
                    posToScroll = it
                }

                binding.messageListRecycler.smoothScrollToPosition(posToScroll)

            }
        })

        viewModel.newMsgReceivedEvent.observe(viewLifecycleOwner, Observer {event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                scrollWhenNewMsgReceivedIfNeeded()
            }
        })
    }

    private fun getAutorizdUser() = FirebaseAuth.getInstance().currentUser?.uid

    companion object {

        const val TAG = "ChatFragment"
        const val TITLE = "title"
        const val IS_FIRST = "isFirst"

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