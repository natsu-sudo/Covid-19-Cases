package com.assignment.covid_19cases.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.assignment.covid_19cases.pojo.CountriesDetail

@Database(entities = [CountriesDetail::class],version = 1)
abstract class CountryDatabase: RoomDatabase() {
    abstract fun countryListDao():CountryListDao
    companion object{
        @Volatile
        private var instance:CountryDatabase?=null

        fun getInstance(context: Context)= instance?: synchronized(this){
            Room.databaseBuilder(context.applicationContext,CountryDatabase::class.java
                ,"country_database").build().also {
                instance=it
            }
        }
    }
}