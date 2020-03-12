package com.example.myapplication.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.R
import com.example.myapplication.core.BaseFragment
import com.example.myapplication.core.FragmentType
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class SplashFragment: BaseFragment() {

    @Inject
    lateinit var navigator: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getMainComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideToolbar()

        view.postDelayed( {
            if (!isUserAutorized()) {
                navigator.showLoginScreen()
            } else {
                navigator.showMyChatsScreen()
            }
        }, 1500)
    }

    override fun enableOptionsMenu(): Boolean = false

    override fun getFragmentType(): FragmentType = FragmentType.NO_MENUFRAGMENT

    private fun isUserAutorized() = FirebaseAuth.getInstance().currentUser != null

    companion object {
        fun newInstance() = SplashFragment()
    }
}