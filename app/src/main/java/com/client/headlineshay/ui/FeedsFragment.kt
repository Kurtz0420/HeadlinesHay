package com.client.headlineshay.ui

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.client.headlineshay.databinding.FragmentFeedsBinding
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feeds.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedsFragment : Fragment(), ArticlesListAdapter.Interaction{


    public var query = "bitcoin"

    private lateinit var navController: NavController
    private var binding: FragmentFeedsBinding? = null //FragmentMainBinding : Auto-Generated


    //injecting viewmodel
    private val viewModel : MainViewModel by viewModels()

    lateinit var articlesListAdapter: ArticlesListAdapter

    private lateinit var scrollListener: RecyclerView.OnScrollListener


    private var linearLayoutManager = LinearLayoutManager(activity)

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()


    //pagination vars
    var isLastPage: Boolean = false
    var isLoading: Boolean = false


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


//        binding!!.btnFeeds.setOnClickListener(){
//            navController!!.navigate(R.id.action_feedsFragment_to_FullArticleFragment)
//        }


//        Wed Aug 12 15:48:27 GMT+05:00 2020
//        E M dd HH:mm:ss O yyyy



        initRecyclerView()
        setRecyclerViewScrollListener()
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)

        //all the items in db from latest to oldest are fetched here

    }

    private fun initRecyclerView() {
        binding!!.recyclerView.apply {

            layoutManager = linearLayoutManager
            articlesListAdapter = ArticlesListAdapter(this@FeedsFragment)
            adapter = articlesListAdapter
        }
    }


    private fun setRecyclerViewScrollListener() {
//        scrollListener = object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                val totalItemCount = recyclerView.layoutManager!!.itemCount
//                if (totalItemCount == lastVisibleItemPosition + 1) {
//
//                    Log.d("FeedsFragment", "subscribe : Load new list")
//                    viewModel.pageNo++
//                    viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
//                    recyclerView.removeOnScrollListener(scrollListener)
//                }
//            }
//        }
        recyclerView?.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call loadmore items to get more data
                loadMoreData()
            }
        })
    }

    private fun loadMoreData() {
//        viewModel.pageNumber.value?.let { a ->
//
//            viewModel.pageNumber.value = a + 1
//
//        }
        viewModel.pageNo++
        viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
        Log.d(TAG, "subscribe: Loading More Data Page: ${viewModel.pageNo}")
    }


    companion object {
        private const val TAG = "FeedsFragment"
    }



    private fun subscribeObservers(){
        viewModel.mutableArticlesListLive.observe(viewLifecycleOwner, Observer { list ->

            //mutableLIst
            for(article in list){
                Log.d(TAG, "subscribeObservers: ${article.title} : ${article.publishedAt} : Country:  \n")
            }
            articlesListAdapter.submitList(list)
            isLoading = false
            Log.d(TAG, "subscribeObservers: ${list.size}")

        })


        viewModel.dataStateLive.observe(viewLifecycleOwner, Observer { dataState ->

            when(dataState){
                is DataState.Success<List<ArticleLocal>> -> {

                    Log.d(TAG, "subscribeObservers: Sealed Class DataState.Success Size : ${dataState.data.size} ")







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

    override fun onItemSelected(position: Int, item: ArticleLocal) {
        viewModel.query = "bitcoin"
        viewModel.setStateEvent(MainStateEvent.SearchNews)
        Toast.makeText(activity, "Clicked : ${item.title}", Toast.LENGTH_LONG).show();
    }


}