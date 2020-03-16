package com.example.myapplication.ui.login

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
import androidx.core.view.ViewCompat
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.databinding.LoginFragmentBinding
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.utils.makeGone
import com.example.myapplication.utils.makeInvisible
import com.example.myapplication.utils.makeVisible
import kotlinx.android.synthetic.main.login_fragment.*
import javax.inject.Inject

class LoginFragment: BaseFragment(), TextView.OnEditorActionListener {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, vmFactory).get(LoginViewModel::class.java)
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
        val binding: LoginFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

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

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideToolbar()

        (passwordEt as EditText).setOnEditorActionListener(this)

        view.rootView.setOnApplyWindowInsetsListener {v, insets ->
            logoLL?.setPaddingRelative(0, insets.systemWindowInsetTop, 0 , 0)

            val systemWindowInsets = with(insets) {
                Rect(
                    systemWindowInsetLeft,
                    systemWindowInsetTop,
                    systemWindowInsetRight,
                    systemWindowInsetBottom
                )
            }

            val keyboardHeight = systemWindowInsets.bottom
            if (keyboardHeight > 130) {
                onKeyboardVisible()

            } else {
                onKeyboardHidden()
            }

            return@setOnApplyWindowInsetsListener  insets.consumeSystemWindowInsets()
        }
    }

    override fun onStop() {
        super.onStop()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    private fun onKeyboardVisible() {
        logoLL?.makeGone()
//        textView5?.makeGone()
        view?.requestLayout()
    }

    private fun onKeyboardHidden() {
        logoLL?.makeVisible()
//        textView5?.makeVisible()
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        if (p1 == EditorInfo.IME_ACTION_DONE) {
            if (!viewModel.buttonDisabled.get()) {
                viewModel.onSignInButtonClicked()
            }
        }
        return true
    }

    companion object {
        fun newInstance() = LoginFragment()

        const val TAG = "LoginFragment"
    }
}