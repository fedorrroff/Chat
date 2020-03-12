package com.example.myapplication.ui.signup

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.SignupFragmentBinding
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SignUpFragment: BaseFragment() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(SignUpViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: SignupFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.signup_fragment, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.bind(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners(view)
    }

    private fun addListeners(view: View) {
        viewModel.showErrorSnackbarEvent.observe(viewLifecycleOwner, Observer {event ->
            event?.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(view, it, Snackbar.LENGTH_LONG).apply {
                    setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorAccent))
                    show()
                }
            }
        })
    }

    override fun getToolbarTitle(): CharSequence = "Registration"

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }
}