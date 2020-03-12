package com.example.myapplication.ui.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.MyprofileFragmentBinding
import com.example.myapplication.models.ChatUser
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ProfileFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: ProfileViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(ProfileViewModel::class.java)
    }

    private val user: ChatUser by lazy { (arguments?.getSerializable(TAG) as ChatUser) }

    private val isCurrentUser: Boolean by lazy { user.id == FirebaseAuth.getInstance().currentUser?.uid }

    private lateinit var binding: MyprofileFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.myprofile_fragment, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.bind(this)

        setUpProfileInfo()
        return binding.root
    }

    private fun setUpProfileInfo() {
        viewModel.name.postValue(user.name)
        viewModel.tag.postValue(user.id)
    }

    companion object {

        const val TAG = "ProfileFragment"

        fun newInstance(chatUser: ChatUser): ProfileFragment {
            val fragment = ProfileFragment()

            fragment.arguments = Bundle().apply {
                putSerializable(TAG, chatUser)
            }
            return fragment
        }
    }
}