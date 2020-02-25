package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.di.MainComponentHolder
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.toolbar.IMenuDelegate
import com.example.myapplication.toolbar.IToolbarHolder
import com.example.myapplication.toolbar.IToolbarProvider
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IToolbarHolder {

    private val navigator: INavigation = Navigation(this)

    @Inject
    lateinit var menuDelegate: IMenuDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val mainComponentHolder = MainComponentHolder.getInstance()
        mainComponentHolder.initDaggerComponent(this)

        if(savedInstanceState == null) {
            navigator.showSplashScreen()
        }
    }

    override fun useCustomToolbar(toolbar: Toolbar?) {
        appBarLayout?.visibility = View.GONE

        fragment_container.setPadding(0, 0, 0, 0)
        menuDelegate.useCustomToolbar(toolbar)
        invalidateToolbarTitles()
    }

    override fun useBaseToolbar(navigationIcon: Int?) {
        appBarLayout.visibility = View.VISIBLE

        menuDelegate.useBaseToolbar(navigationIcon)
        fragment_container.setPadding(0, 0, 0, 0)
        invalidateToolbarTitles()
    }

    override fun invalidateToolbarTitles() {
        invalidateToolbarTitle()
        invalidateToolbarSubTitle()
    }

    override fun invalidateToolbarTitle() {
        supportActionBar?.title = getToolbarProvider()?.getToolbarTitle()
    }

    override fun invalidateToolbarSubTitle() {
        supportActionBar?.title = getToolbarProvider()?.getToolbarSubtitle()
    }

    override fun showNavigationArrow() {
        menuDelegate.showNavigationArrow()
    }

    override fun hideNavigationArrow() {
        menuDelegate.hideNavigationArrow()
    }

    override fun hideToolbar() {
        appBarLayout.makeGone()
    }

    override fun showToolbar() {
        appBarLayout.makeVisible()
    }

    override fun getSupportActionBarActivity(): ActionBar? = supportActionBar

    fun View.makeGone() {
        visibility = View.GONE
    }

    fun View.makeVisible() {
        visibility = View.VISIBLE
    }

    private fun getToolbarProvider() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? IToolbarProvider

    companion object {
        const val TAG = "Main Activity"
    }
}
