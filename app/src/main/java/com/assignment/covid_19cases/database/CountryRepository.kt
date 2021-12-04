package com.assignment.covid_19cases.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.assignment.covid_19cases.network.ApiService
import com.assignment.covid_19cases.pojo.CountriesDetail
import com.assignment.covid_19cases.pojo.ErrorCode
import com.assignment.covid_19cases.pojo.LoadingStatus
import java.net.UnknownHostException

private const val TAG = "CountryRepository"
class CountryRepository(context: Context) {
    private val countryListRepo=CountryDatabase.getInstance(context).countryListDao()
        private val apiService by lazy { ApiService.getInstance() }

    fun getCountryList(): LiveData<List<CountriesDetail>> {
        return countryListRepo.geCountyListFromDB()
    }

    suspend fun deleteFromDataBase(){
        countryListRepo.deleteAllData()
    }

    fun getSimilarListOfCountry(name:String): LiveData<List<CountriesDetail>> {
        return countryListRepo.searchForCountry(name)
    }

    suspend fun fetchFromNetwork()=try {
        val result=apiService.getListOfCountries()
        Log.d(TAG, "fetchFromNetwork 1: $result")
        if (result.isSuccessful){
            val listOfCountry=result.body()
            Log.d(TAG, "fetchFromNetwork: $listOfCountry")
            listOfCountry?.let {
                countryListRepo.insertIntoDb(it)
            }
            LoadingStatus.success()
        }else{
            Log.e(TAG, "fetchFromNetwork: No Data")
            LoadingStatus.error(ErrorCode.NO_DATA)
        }
    }catch (ex: UnknownHostException){
        Log.e(TAG, ex.message.toString() )
        LoadingStatus.error(ErrorCode.NETWORK_ERROR)
    }catch (ex: Exception){
        Log.e(TAG, ex.message.toString())
        LoadingStatus.error(ErrorCode.UNKNOWN_ERROR)
    }

}