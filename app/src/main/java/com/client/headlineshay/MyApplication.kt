package com.client.headlineshay

import android.app.Application
import android.os.Build
import android.util.Log
import com.client.headlineshay.network.enums.AvailableCountries
import com.client.headlineshay.network.enums.Category
import com.client.headlineshay.network.enums.Country
import com.client.headlineshay.utils.AppPreferences
import com.facebook.stetho.Stetho
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication: Application(){

    private var country: String = ""

    companion object {
        private const val TAG = "MyApplication"
    }

    override fun onCreate() {
        super.onCreate()

        //for database monitoring, (remove in production)
        initStetho()

        //one-time init of SharedPrefs, Usable everywhere
        iniSharedPrefs()

        //sets country, type of news
        firstRunConfig();



    }

    private fun firstRunConfig() {

        if(!AppPreferences.app_first_run){
            setConfigForNews()
            AppPreferences.app_first_run  = true
        }
    }

    private fun setConfigForNews() {
        //get the country from device
        country = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.resources.configuration.locales.get(0).country.toLowerCase()
        } else {
            this.resources.configuration.locale.country.toLowerCase()
        }

        //search it in the list of available
        val isAvailable = AvailableCountries.isPresent(country)

        //set if present, otherwise US default
        AppPreferences.news_country = if(isAvailable!!) country else Country.UNITED_STATES.value

        //set category as General by default
        AppPreferences.news_category = Category.GENERAL.value
    }

    private fun iniSharedPrefs() {
        AppPreferences.init(this)
    }

    private fun initStetho() {
        Stetho.initializeWithDefaults(this);
    }


}