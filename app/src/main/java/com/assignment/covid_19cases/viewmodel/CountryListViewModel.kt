package com.assignment.covid_19cases.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.covid_19cases.database.CountryRepository
import com.assignment.covid_19cases.pojo.CountriesDetail
import com.assignment.covid_19cases.pojo.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CountryListViewModel(context: Context):ViewModel() {
    private val database= CountryRepository(context)
    private var liveStatus= MutableLiveData<LoadingStatus>()
    val getList:LiveData<List<CountriesDetail>> = getCountryList()
    val status get() = liveStatus

    private fun getCountryList(): LiveData<List<CountriesDetail>> {
        return database.getCountryList()
    }

    fun fetchFromNetwork(){
        liveStatus.value=LoadingStatus.loading()
        viewModelScope.launch {
            liveStatus.value = withContext(Dispatchers.IO){
                database.fetchFromNetwork()
            }!!
        }
    }

    fun deleteData() {
        viewModelScope.launch {
            database.deleteFromDataBase()
        }
    }




}