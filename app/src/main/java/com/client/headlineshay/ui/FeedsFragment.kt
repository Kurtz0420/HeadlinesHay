package com.client.headlineshay.ui

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.client.headlineshay.R
import com.client.headlineshay.databinding.FragmentFeedsBinding
import com.client.headlineshay.network.models.local.ArticleLocal
import com.client.headlineshay.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feeds.*
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedsFragment : Fragment(), ArticlesListAdapter.Interaction, View.OnClickListener{


    public var query = "bitcoin"

    private lateinit var navController: NavController
    private var binding: FragmentFeedsBinding? = null //FragmentMainBinding : Auto-Generated


    //injecting viewmodel
    private val viewModel : MainViewModel by viewModels()

    lateinit var articlesListAdapter: ArticlesListAdapter

    //pagination vars
    var isLastPage: Boolean = false
    var isLoading: Boolean = false

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


        initRecyclerView()
        setRecyclerViewScrollListener()
        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)



        binding!!.backBtnSearch.setOnClickListener(backBtnCL)
        binding!!.floatingSearchBtn.setOnClickListener(floatingSearchCL)


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

    }

    private fun transit(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(binding!!.root)
        }
    }

    /*Switches UI and data from search to latest News*/
    private fun triggerLatestNews() {
        binding!!.searchBarEt.text = null
        searchNewsFlag = false
        transit()
        binding!!.floatingSearchBtn.visibility = View.VISIBLE
        articlesListAdapter.submitList(emptyList())
        viewModel.pageNo = 1
        viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
    }

    /*Switches UI and data from latest News to Search*/
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

        recyclerView?.addOnScrollListener(object : PaginationScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call load more items to get more data
                loadMoreData()
            }
        })
    }

    private fun loadMoreData() {

        if(!searchNewsFlag){
            viewModel.pageNo++
            viewModel.setStateEvent(MainStateEvent.GetLatestNewsFrom)
        }else{
            viewModel.pageNo++
            viewModel.setStateEvent(MainStateEvent.SearchNews)
        }
    }


    companion object {
        private const val TAG = "FeedsFragment"
    }



    private fun subscribeObservers(){


        viewModel.dataStateLive.observe(viewLifecycleOwner, Observer { dataState ->

            when(dataState){
                is DataState.Success<List<ArticleLocal>> -> {

                    Log.d(TAG, "subscribeObservers: Sealed Class DataState.Success Size : ${dataState.data.size} ")


                    if(searchNewsFlag){

                        loadDataForSearchNews(dataState.data)


                    }else{

                        loadDataForLatestNews(dataState.data)

                    }

                }
                is DataState.Error -> {
                    Toast.makeText(activity, "Something went wrong while loading data : ${dataState.exception.message}", Toast.LENGTH_LONG).show();
                }
                is DataState.Loading -> {
//                    displayProgressBar(true)
                }
            }
        })
    }


    private fun loadDataForLatestNews(data: List<ArticleLocal>) {
        isLastPage = articlesListAdapter.itemCount == data.size
        val list = data.toMutableList()
        list.add(ArticleLocal(0,"","","","Loading","","","","","",""))
        articlesListAdapter.submitList(list)
        isLoading = false

    }


    private fun loadDataForSearchNews(data: List<ArticleLocal>) {
        isLastPage = articlesListAdapter.itemCount == data.size
        searchNewList.addAll(data)
        searchNewList.add(ArticleLocal(0,"","","","Loading","","","","","",""))
        articlesListAdapter.submitList(searchNewList.toList())
        isLoading = false
    }


    private fun displayProgressBar(isDisplayed: Boolean){
//        progress_bar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }




    override fun onItemSelected(position: Int, item: ArticleLocal) {
        gotoFullArticleFrag(item)
    }

    override fun onHeadlineSelected(position: Int, item: ArticleLocal) {
        gotoFullArticleFrag(item)
    }


    private fun gotoFullArticleFrag(item : ArticleLocal){
        val bundle = bundleOf("url" to item.url)
        navController.navigate(R.id.action_feedsFragment_to_FullArticleFragment, bundle)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.backBtnSearch -> {

            }
            R.id.floatingSearchBtn -> {


            }
        }
    }


    val backBtnCL = View.OnClickListener{
        Log.d(TAG, "onClick: backBtn")
        //check if text isnotEmpty, then switch to latest news
        if(binding!!.searchBarEt.text.isNotEmpty()){
            triggerLatestNews()
        }
    }

    val floatingSearchCL = View.OnClickListener{
        //hide/show searchBtn depending upon visibility
        if(binding!!.searchLayout.visibility == View.GONE){
            transit()
            binding!!.searchLayout.visibility = View.VISIBLE
        }else{
            transit()
            binding!!.searchLayout.visibility = View.GONE
        }
    }




}