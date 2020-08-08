package com.client.headlineshay.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.client.headlineshay.R
import com.client.headlineshay.databinding.FragmentFullArticleBinding


class FullArticleFragment : Fragment() {


    private var binding: FragmentFullArticleBinding?= null
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFullArticleBinding.inflate(inflater,container,false)
        return binding!!.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding!!.btnFullArticle.setOnClickListener(){
            navController!!.navigate(R.id.action_fullArticleFragment_to_FeedsFragment)
        }




    }


}