package com.example.myapplication.core

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(enableOptionsMenu())
    }

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

    open fun enableOptionsMenu() = true

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    fun initToolbar() {
        toolbarHolder.useBaseToolbar(provideNavigationIcon())
    }

    override fun getToolbarTitle(): CharSequence = ""

    override fun getToolbarSubtitle(): CharSequence? = null

    open fun showNavigationArrow(): Boolean = true

    open fun provideNavigationIcon(): Int? = null

    open fun getFragmentType() = FragmentType.MENU_FRAMENT
}