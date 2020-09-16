package com.client.headlineshay.ui

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.repository.MainRepository
import com.client.headlineshay.utils.AppPreferences
import com.client.headlineshay.utils.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class MainViewModel
@ViewModelInject
constructor(
        private val mainRepository: MainRepository,
        @Assisted private val savedStateHandle : SavedStateHandle
) : ViewModel() {


    var country  = AppPreferences.news_country
    var query = "bitcoin"
    var category = AppPreferences.news_category
    var pageNo = 1



    private val mutable_dataState : MutableLiveData<DataState<List<ArticleLocal>>> = MutableLiveData<DataState<List<ArticleLocal>>>().apply {

        value = null
    }

    val dataStateLive : LiveData<DataState<List<ArticleLocal>>>
        get() = mutable_dataState











    /*Function for setting state event
    * Interpreting state event
    * work accordingly with each state event*/

    fun setStateEvent(mainStateEvent: MainStateEvent){
        viewModelScope.launch {
            when(mainStateEvent){

                is MainStateEvent.GetLatestNewsFrom ->{

                    mainRepository.getLatestNews(pageNo)
                            .onEach {
                                dataState -> // new dataState with data
                            mutable_dataState.value = dataState
                            }
                            .launchIn(viewModelScope)

                }



                is MainStateEvent.SearchNews ->{
                    val articles = mainRepository.searchNews(query,pageNo)
                    articles.onEach {
                        dataState ->
                        mutable_dataState.value = dataState
                    }
                            .launchIn(viewModelScope)

                }

                is MainStateEvent.None -> {
                    TODO("Code For None Event")
                }
            }
        }
    }




}


/*For MVI, we declare events that we can later fire off
* Here we can add all of our events like the current ones
*
* */
sealed class MainStateEvent{


    object GetLatestNewsFrom : MainStateEvent()

    object SearchNews : MainStateEvent()

    object None : MainStateEvent()

}