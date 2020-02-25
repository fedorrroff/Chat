package com.example.myapplication.toolbar

interface IToolbarProvider {
    fun getToolbarTitle() : CharSequence
    fun getToolbarSubtitle() : CharSequence?
}