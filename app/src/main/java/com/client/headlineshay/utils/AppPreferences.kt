package com.client.headlineshay.utils

import android.content.Context
import android.content.SharedPreferences
import com.client.headlineshay.network.enums.Category
import com.client.headlineshay.network.enums.Country




/*Responsible for everything SharedPreferences related stuff*/
object AppPreferences {
    private const val NAME = "SpinKotlin"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val NEWS_CATEOGRY = Pair("category", Category.GENERAL.value)
    private val NEWS_COUNTRY = Pair("country", Country.UNITED_STATES.value)
    private val FIRST_RUN = Pair("app_first_run", false)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var news_country: String?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString(NEWS_COUNTRY.first, NEWS_COUNTRY.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString(NEWS_COUNTRY.first, value)
        }

    var news_category: String?
        get() = preferences.getString(NEWS_CATEOGRY.first, NEWS_CATEOGRY.second)
        set(value) = preferences.edit {
            it.putString(NEWS_CATEOGRY.first, value)
        }

    var app_first_run: Boolean

        get() = preferences.getBoolean(FIRST_RUN.first, FIRST_RUN.second)
        set(value) = preferences.edit {
            it.putBoolean(FIRST_RUN.first, value)
        }
}