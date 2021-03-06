package com.example.myapplication.toolbar

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_chat.*

class MenuDelegate(private val activity: AppCompatActivity) : IMenuDelegate {

    private var navigationIconVisibility = true

    override fun onCreate(savedInstanceState: Bundle?) {
        setToolbar(activity.toolbar)
    }

    override fun useBaseToolbar(navigationIcon: Int?) {
        setToolbar(activity.toolbar, navigationIcon)
    }

    override fun showNavigationArrow() {
        navigationIconVisibility = true
    }

    override fun hideNavigationArrow() {
        navigationIconVisibility = false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setToolbar(toolbar: Toolbar?, navigationIcon: Int? = null, naviationAction: (() -> Unit)? = null) {
        activity.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(true)

            //TODO add action
            toolbar?.setNavigationOnClickListener {
                onBackPressed()
            }

        }

        if (navigationIconVisibility) {
            if (navigationIcon == null) {
                toolbar?.navigationIcon = activity.getDrawable(R.drawable.ic_arrow_back_black_24dp)
            } else {
                toolbar?.navigationIcon = activity.getDrawable(navigationIcon)
            }
        } else {
            toolbar?.navigationIcon = null
        }
    }
}