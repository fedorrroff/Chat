package com.example.myapplication.ui.signup

import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.SignupFragmentBinding
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.utils.makeGone
import com.example.myapplication.utils.makeVisible
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.signup_fragment.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.signup_fragment.passwordEt as passwordEt1
import com.google.android.material.resources.MaterialResources.getDimensionPixelSize



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

    override fun onStart() {
        super.onStart()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addListeners(view)
        (passwordEtSignup as EditText).setOnEditorActionListener(this)

        view.rootView.setOnApplyWindowInsetsListener {v, insets ->
            (activity as MainActivity).mainActivityRoot.setPaddingRelative(0, insets.systemWindowInsetTop, 0, 0)

            val systemWindowInsets = with(insets) {
                Rect(
                    systemWindowInsetLeft,
                    systemWindowInsetTop,
                    systemWindowInsetRight,
                    systemWindowInsetBottom
                )
            }

            val keyboardHeight = systemWindowInsets.bottom
            val navBarHeight = getNavBarHeight()
            if (keyboardHeight > navBarHeight) {
                onKeyboardVisible(keyboardHeight - navBarHeight)

            } else {
                onKeyboardHidden()
            }

            return@setOnApplyWindowInsetsListener  insets.consumeSystemWindowInsets()
        }
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

    private fun onKeyboardVisible(height: Int) {
        logo_signup?.makeGone()
        view?.setPadding(0,0,0, height)
        view?.requestLayout()
    }

    private fun getNavBarHeight(): Int {
        val resources = context?.resources
        val resourceId = resources?.getIdentifier("navigation_bar_height", "dimen", "android") ?: 0
        return if (resourceId > 0) {
            resources?.getDimensionPixelSize(resourceId)!!
        } else 0
    }

    private fun onKeyboardHidden() {
        logo_signup?.makeVisible()
        view?.setPadding(0,0,0, 0)
    }

    override fun getToolbarTitle(): CharSequence = "Registration"

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }
}