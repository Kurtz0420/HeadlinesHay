package com.client.headlineshay.utils



/*Limited types while data transactions : Simplified Version*/

sealed class DataState<out R>{


    data class Success<T> (var data: T) :DataState<T>()
    data class Error(val exception: Exception) :DataState<Nothing>()
    object Loading :DataState<Nothing>()



    val DataState<*>.succeeded
        get() = this is Success && data != null


    fun <T> DataState<T>.successOr(fallback: T): T{
        return (this as? Success<T>)?.data ?: fallback
    }

}