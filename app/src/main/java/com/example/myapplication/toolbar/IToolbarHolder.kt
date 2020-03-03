package com.example.myapplication.toolbar

import androidx.appcompat.app.ActionBar

interface IToolbarHolder {

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