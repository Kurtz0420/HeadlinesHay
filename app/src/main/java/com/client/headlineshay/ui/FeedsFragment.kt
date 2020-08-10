package com.client.headlineshay.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.client.headlineshay.R
import com.client.headlineshay.databinding.FragmentFeedsBinding
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedsFragment : Fragment(){

    private lateinit var navController: NavController
    private var binding: FragmentFeedsBinding? = null //FragmentMainBinding : Auto-Generated


    //injecting viewmodel
    private val viewModel : MainViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedsBinding.inflate(inflater,container,false)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val country = requireContext().resources.configuration.locales.get(0).country.toLowerCase()
            Toast.makeText(activity, " Country : $country", Toast.LENGTH_SHORT).show()
        }

        binding!!.btnFeeds.setOnClickListener(){
            navController!!.navigate(R.id.action_feedsFragment_to_FullArticleFragment)
        }



        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetArticles)


    }


    companion object {
        private const val TAG = "FeedsFragment"
    }



    private fun subscribeObservers(){
        viewModel.dataState.observe(this, Observer { dataState ->
            when(dataState){
                is DataState.Success<List<ArticleLocal>> -> {

                    Log.d(TAG, "subscribeObservers: ${dataState.data} ")
                    for(article in dataState.data){
                        Log.d(TAG, "subscribeObservers: ${article.title} \n")
                    }
                    Log.d(TAG, "subscribeObservers: ${dataState.data.size} ")
//                    displayProgressBar(false)
//                    appendBlogTitles(dataState.data)
                }
                is DataState.Error -> {
                    Toast.makeText(activity, "Error Occured", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "subscribeObservers: ${dataState.exception.message}")
//                    displayProgressBar(false)
//                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
//                    displayProgressBar(true)
                }
            }
        })
    }


    private fun displayProgressBar(isDisplayed: Boolean){
//        progress_bar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }


    

}