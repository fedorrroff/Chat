package com.example.myapplication.toolbar

import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBar

interface IToolbarHolder {

    fun useCustomToolbar(toolbar: Toolbar?)
    fun useBaseToolbar(navigationIcon: Int?)
    fun invalidateToolbarTitles()
    fun invalidateToolbarTitle()
    fun invalidateToolbarSubTitle()
    fun showNavigationArrow()
    fun hideNavigationArrow()
    fun hideToolbar()
    fun showToolbar()
    fun getSupportActionBarActivity(): ActionBar?
}