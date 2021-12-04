package com.assignment.covid_19cases.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CountryListViewModelFactory(private val context: Context ):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java).newInstance(context.applicationContext)
    }
}