package com.example.myapplication.ui.signup

import android.app.ProgressDialog.show
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.SignupFragmentBinding
import com.example.myapplication.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.signup_fragment.*
import javax.inject.Inject

class SignUpFragment: BaseFragment(), TextView.OnEditorActionListener {

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

        viewModel.navigation = Navigation(requireActivity() as AppCompatActivity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners(view)
        lastNameEt.setOnEditorActionListener(this)
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

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_DONE) {
            if (viewModel.buttonEnabled.get()) {
                viewModel.onSignUpButtonClicked()
            }
        }
        return true
    }

    override fun getToolbarTitle(): CharSequence = "Registration"

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }
}