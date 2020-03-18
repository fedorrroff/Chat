package com.example.myapplication.ui.mychats

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.MychatsFragmentBinding
import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.ui.mychats.adapters.MyChatsAdapter
import com.example.myapplication.utils.toChatUser
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MyChatsFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: MyChatsViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(MyChatsViewModel::class.java)
    }

    private val chatAdapter = MyChatsAdapter()

    private lateinit var binding: MychatsFragmentBinding

    private lateinit var currentUser: CurrentUser

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

        viewModel.navigation = Navigation(requireActivity() as AppCompatActivity)

        setUpChatList()
        setUpCurrentUser()

        subscribeToEvents(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // вызов костыля ебаного
        viewModel.updateLastMsgs()
    }

    override fun getToolbarTitle(): CharSequence = "My App"

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_mychats, menu)
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
                when(it) {
                    is Resource.Success -> {
                        val sortedChats = it.data.sortedByDescending { chat -> chat.messages.lastOrNull()?.timestamp }
                        this.replaceItems(sortedChats.toMutableList())
                    }
                    is Resource.Failure -> {

                    }
                    is Resource.Loading -> {

                    }
                }
            })
        }
    }

    private fun subscribeToEvents(view: View) {
        viewModel.showNoElementToastEvent.observe(viewLifecycleOwner, Observer {event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    show()
                }
            }
        })

        viewModel.showToastSucseedEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                    show()
                }
            }
        })

        viewModel.showSearchDialogEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                val searchDialog = SearchUserDialogFragment()
                searchDialog.show(childFragmentManager, TAG)
            }
        })

        viewModel.pushChatToTopEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                chatAdapter.pushChatToTop(it)
            }
        })

        viewModel.updateSingleChatEvent.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                chatAdapter.update(it)
            }
        })
    }

    private fun setUpCurrentUser() {
        viewModel.currentUser.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Resource.Success -> {
                    currentUser = it.data!!
                }
                is Resource.Failure -> {

                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_signout_mychats -> viewModel.onSignOutButtonClicked()
            R.id.action_check_profile -> viewModel.onProfileButtonClicked(currentUser.toChatUser())
        }
        return true
    }

    override fun provideNavigationIcon(): Int? = R.drawable.ic_menu_black_24dp

    companion object {

        const val TAG = "MyChatsFragment"

        fun newInstance() = MyChatsFragment()
    }
}