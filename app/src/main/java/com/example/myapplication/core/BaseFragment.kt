package com.example.myapplication.core

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.di.ActivityModule
import com.example.myapplication.di.DaggerMainComponent
import com.example.myapplication.di.MainComponent
import com.example.myapplication.toolbar.IToolbarHolder
import com.example.myapplication.toolbar.IToolbarProvider

abstract class BaseFragment: Fragment(), IToolbarProvider {

    protected lateinit var toolbarHolder: IToolbarHolder

    protected fun getMainComponent(): MainComponent = DaggerMainComponent
                                                        .builder()
                                                        .activityModule(ActivityModule(requireActivity() as AppCompatActivity))
                                                        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        toolbarHolder = context as IToolbarHolder
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (showNavigationArrow()) {
            toolbarHolder.showNavigationArrow()
        } else {
            toolbarHolder.hideNavigationArrow()
        }

        initToolbar()
    }

    fun initToolbar() {
        val customToolbar = provideCustomToolbar()
        when {
            customToolbar != null -> toolbarHolder.useCustomToolbar(customToolbar)
            else -> toolbarHolder.useBaseToolbar(provideNavigationIcon())
        }
    }

    open fun showNavigationArrow(): Boolean = true

    open fun provideCustomToolbar(): Toolbar? = null

    open fun provideNavigationIcon(): Int? = null
}