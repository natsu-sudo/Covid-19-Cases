package com.assignment.covid_19cases.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.covid_19cases.pojo.CountriesDetail

@Dao
interface CountryListDao {

    @Query("select * from country_table order by name")
    fun geCountyListFromDB(): LiveData<List<CountriesDetail>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoDb(list: List<CountriesDetail>)

    @Query("delete from country_table")
    suspend fun deleteAllData()
}