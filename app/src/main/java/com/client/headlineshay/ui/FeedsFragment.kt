package com.client.headlineshay.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.client.headlineshay.R
import com.client.headlineshay.databinding.FragmentFeedsBinding


class FeedsFragment : Fragment(){

    private lateinit var navController: NavController
    private var binding: FragmentFeedsBinding? = null //FragmentMainBinding : Auto-Generated




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



    }


    companion object {
        private const val TAG = "FeedsFragment"
    }


}