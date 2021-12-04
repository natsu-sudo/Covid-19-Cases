package com.assignment.covid_19cases.network

import com.assignment.covid_19cases.BuildConfig
import com.assignment.covid_19cases.constants.Constants
import com.assignment.covid_19cases.pojo.CountriesDetail
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {

    companion object{


        private val retrofitService by lazy {
            Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }

        fun getInstance():ApiService= retrofitService
    }

    @GET("help/countries")
    @Headers(Constants.RAPID_HOST_API+":"+Constants.RAPID_HOST_API_VALUE,
        Constants.RAPID_HOST_API_KEY+":"+BuildConfig.RAPID_API_KEY)
    suspend fun getListOfCountries(): Response<List<CountriesDetail>>



}