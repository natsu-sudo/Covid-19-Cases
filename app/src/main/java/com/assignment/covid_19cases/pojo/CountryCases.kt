package com.assignment.covid_19cases.pojo

data class CountryCases(
    val country:String,
    val code:String,
    val confirmed:String,
    val recovered :String,
    val critical:String,
    val deaths:String,
    val latitude:String,
    val longitude:String,
    val lastChange:String?,
    val lastUpdate:String?
)