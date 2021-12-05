package com.assignment.covid_19cases.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.assignment.covid_19cases.network.ApiService
import com.assignment.covid_19cases.pojo.CountryCases
import com.assignment.covid_19cases.pojo.ErrorCode
import com.assignment.covid_19cases.pojo.LoadingStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import java.text.NumberFormat
import java.util.*


private const val TAG = "DetailViewModel"
class DetailViewModel(private val countryCode:String) :ViewModel(){
    private val apiService by lazy { ApiService.getInstance() }
    private val _countryDetail=MutableLiveData<CountryCases>()
    val countryDetail get() = _countryDetail
    val death:LiveData<String> = Transformations.switchMap(_countryDetail,::getDeaths)
    val recovered:LiveData<String> = Transformations.switchMap(_countryDetail,::getRecovered)
    val getActiveCase:LiveData<String> = Transformations.switchMap(_countryDetail,::getActiveCase)
    val getCritical:LiveData<String> = Transformations.switchMap(_countryDetail,::getCritical)
    val getLastUpdateTime=Transformations.switchMap(_countryDetail,::getUpdatedTime)
    val getLastChangedTime=Transformations.switchMap(_countryDetail,::getChangedTime)

    private fun getChangedTime(countryCases: CountryCases):  LiveData<String> {
        return MutableLiveData<String>(countryCases.lastChange?.substring(0,10))
    }

    private fun getUpdatedTime(countryCases: CountryCases): LiveData<String> {
        return MutableLiveData<String>(countryCases.lastUpdate?.substring(0,10))
    }

    fun getCountryCasesFromNetwork(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val result=apiService.getCountryCases(countryCode)
                    Log.d(TAG, "getCountryCasesFromNetwork 1: $result")
                    if (result.isSuccessful){
                        val detail=result.body()
                        Log.d(TAG, "getCountryCasesFromNetwork: $detail")
                       detail?.let {
                           _countryDetail.postValue(it[0])
                       }
                    }else{
                        Log.e(TAG, "getCountryCasesFromNetwork: No Data")
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
        }
    }

    private fun getDeaths(countryCases: CountryCases): LiveData<String> {
        return MutableLiveData(NumberFormat.getNumberInstance(Locale.US).format(
            countryCases.deaths.toInt()
        ).toString())
    }

    private fun getRecovered(countryCases: CountryCases): LiveData<String> {
        return MutableLiveData(NumberFormat.getNumberInstance(Locale.US).format(
            countryCases.recovered.toInt()
        ).toString())
    }

    private fun getActiveCase(countryCases: CountryCases): LiveData<String> {
        return MutableLiveData(NumberFormat.getNumberInstance(Locale.US).format(
            countryCases.confirmed.toInt()
        ).toString())
    }
    private fun getCritical(countryCases: CountryCases): LiveData<String> {
        return MutableLiveData(NumberFormat.getNumberInstance(Locale.US).format(
            countryCases.critical.toInt()
        ).toString())
    }





}


