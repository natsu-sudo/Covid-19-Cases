package com.assignment.covid_19cases.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_table")
data class CountriesDetail(
    @PrimaryKey
    val name: String,
    val alpha2code: String,
    val alpha3code: String,
    val latitude: Double,
    val longitude: Double
)