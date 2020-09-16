package com.client.headlineshay.ui

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.client.headlineshay.R
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

//    private val lastVisibleItemPosition: Int
//        get() = linearLayoutManager.findLastVisibleItemPosition()


    //pagination vars
    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    var headlinesFlag: Boolean = false



    //when true feeds are populated with search news
    var searchNewsFlag: Boolean = false

    var searchNewList = mutableListOf<ArticleLocal>()


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



        binding!!.floatingSearchBtn.setOnClickListener(View.OnClickListener {
            if(binding!!.searchLayout.visibility == View.GONE){
                transit()
                binding!!.searchLayout.visibility = View.VISIBLE
            }else{
                transit()
                binding!!.searchLayout.visibility = View.GONE
            }

        })

        binding!!.searchBarEt.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action!= KeyEvent.ACTION_DOWN) {

                if(query.isNotEmpty()){
                    triggerSearchNewsWithQuery(binding!!.searchBarEt.text.toString())
                }
                return@OnKeyListener true


            }

            if (keyCode == KeyEvent.KEYCODE_BACK && event.action!= KeyEvent.ACTION_DOWN) {


                //here the code for closing search phase and continuing regular feeds goes
                triggerLatestNews()
                return@OnKeyListener true

            }
            false
        })



        binding!!.backBtnSearch.setOnClickListener(View.OnClickListener {

            if(binding!!.searchBarEt.text.isNotEmpty()){
                triggerLatestNews()
            }

        })
        




        //all the items in db from latest to oldest are fetched here

    }

    fun transit(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding!!.root)
        }
    }

    private fun triggerLatestNews() {
        binding!!.searchBarEt.text = null
        searchNewsFlag = false
        transit()
        binding!!.floatingSearchBtn.visibility = View.VISIBLE
        articlesListAdapter.submitList(emptyList())
        viewModel.pageNo = 1
        viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
    }

    private fun triggerSearchNewsWithQuery(query: String?) {

        searchNewsFlag = true
        binding!!.floatingSearchBtn.visibility = View.GONE
        searchNewList.clear()
        articlesListAdapter.submitList(searchNewList)
        viewModel.pageNo = 1
        viewModel.query = query.toString()
        viewModel.setStateEvent(MainStateEvent.SearchNews)

    }


    private fun initRecyclerView() {
        binding!!.recyclerView.apply {

            layoutManager = LinearLayoutManager(activity)
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
        recyclerView?.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
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

        if(!searchNewsFlag){
            viewModel.pageNo++
            viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
        }else{
            viewModel.pageNo++
            viewModel.setStateEvent(MainStateEvent.SearchNews)
            //when user is out of search, set flag to false
        }

        Log.d(TAG, "subscribe: Loading More Data Page: ${viewModel.pageNo}")
    }


    companion object {
        private const val TAG = "FeedsFragment"
    }



    private fun subscribeObservers(){


        //separate headlines just in first load flag


        viewModel.dataStateLive.observe(viewLifecycleOwner, Observer { dataState ->

            when(dataState){
                is DataState.Success<List<ArticleLocal>> -> {

                    Log.d(TAG, "subscribeObservers: Sealed Class DataState.Success Size : ${dataState.data.size} ")


                    if(searchNewsFlag){
                        isLastPage = articlesListAdapter.itemCount == dataState.data.size
                        searchNewList?.addAll(dataState.data)
                        searchNewList?.add(ArticleLocal(0,"","","","Loading","","","","","",""))
                        articlesListAdapter.submitList(searchNewList!!.toList())
                        Log.d(TAG, "subscribeObservers: SearchNews Success : ${searchNewList!!.size}")

                        isLoading = false

                    }else{
                        Log.d(TAG, "subscribeObservers: Headlines Success")
                        isLastPage = articlesListAdapter.itemCount == dataState.data.size
                        val list = dataState.data.toMutableList()
                        list.add(ArticleLocal(0,"","","","Loading","","","","","",""))
                        articlesListAdapter.submitList(list)
                        isLoading = false
                    }












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


    private fun gotoFullArticleFrag(item : ArticleLocal){
        val bundle = bundleOf("url" to item.url)
        navController.navigate(R.id.action_feedsFragment_to_FullArticleFragment, bundle)
    }

    override fun onItemSelected(position: Int, item: ArticleLocal) {
        gotoFullArticleFrag(item)
    }

    override fun onHeadlineSelected(position: Int, item: ArticleLocal) {
        gotoFullArticleFrag(item)
    }


}