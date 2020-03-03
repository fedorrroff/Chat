package com.example.myapplication.toolbar

import android.os.Bundle
import androidx.appcompat.widget.Toolbar

interface IMenuDelegate {
    fun onCreate(savedInstanceState: Bundle?)
    fun useBaseToolbar(navigationIcon: Int?)
    fun showNavigationArrow()
    fun hideNavigationArrow()
}