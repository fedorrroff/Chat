package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.di.ActivityModule
import com.example.myapplication.di.DaggerMainComponent
import com.example.myapplication.di.MainComponent
import com.example.myapplication.di.MainComponentHolder
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.toolbar.IMenuDelegate
import com.example.myapplication.toolbar.IToolbarHolder
import com.example.myapplication.toolbar.IToolbarProvider
import com.example.myapplication.toolbar.MenuDelegate
import com.example.myapplication.utils.makeGone
import com.example.myapplication.utils.makeVisible
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IToolbarHolder {

    private val menuDelegate: IMenuDelegate = MenuDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val mainComponentHolder = MainComponentHolder.getInstance()
        mainComponentHolder.initDaggerComponent(this)
        getMainComponent().inject(this)

        menuDelegate.onCreate(savedInstanceState)

        if(savedInstanceState == null) {
            mainComponentHolder.getMainComponent().navigator().showSplashScreen()
        }
    }

    private fun getMainComponent() :
        MainComponent = DaggerMainComponent
            .builder()
            .activityModule(ActivityModule(this))
            .build()


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
        supportActionBar?.subtitle = getToolbarProvider()?.getToolbarSubtitle()
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

    private fun getToolbarProvider() = supportFragmentManager.findFragmentById(R.id.fragment_container) as? IToolbarProvider

    companion object {
        const val TAG = "Main Activity"
    }
}
