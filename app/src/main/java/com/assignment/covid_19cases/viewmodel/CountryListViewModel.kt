package com.assignment.covid_19cases.viewmodel

import android.content.Context
import androidx.lifecycle.*
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
    private var countryName = MutableLiveData<String>()
    val listToBeShownOnScreen=MediatorLiveData<List<CountriesDetail>>()
    private val searchList= Transformations.switchMap(countryName,::getSimilarListOfCountry)
    private fun getCountryList(): LiveData<List<CountriesDetail>> {
        return database.getCountryList()
    }
    init {
        listToBeShownOnScreen.addSource(getList){
            listToBeShownOnScreen.value=it
        }
        listToBeShownOnScreen.addSource(searchList){
            listToBeShownOnScreen.value=it
        }

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

    fun setCountryName(name:String){
        countryName.value=name
    }

    private fun getSimilarListOfCountry(countryName: String):LiveData<List<CountriesDetail>>{
        return database.getSimilarListOfCountry(countryName)
    }




}


