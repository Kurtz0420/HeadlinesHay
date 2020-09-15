package com.client.headlineshay.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.client.headlineshay.R
import com.client.headlineshay.databinding.FragmentFullArticleBinding
import com.client.headlineshay.utils.Progress
import com.google.android.material.snackbar.Snackbar
import android.provider.Settings
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN


class FullArticleFragment : Fragment() {

  companion object{
    private const val TAG = "FullArticleFragment"
  }


  private var binding: FragmentFullArticleBinding?= null
  private lateinit var navController: NavController

  private var article_url:String? = "https://www.geeklabs.co.in/" // Change it with your URL





  private var progress: Progress? = null
  private var isLoaded: Boolean = false
  private var doubleBackToExitPressedOnce = false

  override fun onCreateView(
          inflater: LayoutInflater, container: ViewGroup?,
          savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentFullArticleBinding.inflate(inflater,container,false)
    return binding!!.root
  }



  @RequiresApi(Build.VERSION_CODES.M)
  @SuppressLint("SetJavaScriptEnabled")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    navController = Navigation.findNavController(view)
    article_url = arguments?.getString("url")

    Log.d(TAG, "onViewCreated: $article_url")


    binding!!.webView.settings.javaScriptEnabled = true
    binding!!.webView.settings.domStorageEnabled = true
    if (!isOnline()) {
      showToast(getString(R.string.no_internet))
      binding!!.infoTV.text = getString(R.string.no_internet)
      showNoNetSnackBar()
      return
    }


    binding!!.rootView.isFocusableInTouchMode = true
    binding!!.rootView.requestFocus()




    binding!!.webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
      if (keyCode == KeyEvent.KEYCODE_BACK && event.action!= ACTION_DOWN) {

        //if webview has backStack
        if(binding!!.webView.canGoBack()){
          binding!!.webView.goBack()
          return@OnKeyListener true

        }else{
          //if not stack, switch the fragment
          //here the code for switching the fragment goes
          navController.navigate(R.id.action_fullArticleFragment_to_FeedsFragment)
          return@OnKeyListener true

        }

      }
      false
    })


  }

  private fun loadWebView() {
    binding!!.infoTV.text = ""
    binding!!.webView.loadUrl(article_url)
    binding!!.webView.webViewClient = object : WebViewClient() {
      @SuppressLint("NewApi")
      override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url.toString()
        view?.loadUrl(url)
        return super.shouldOverrideUrlLoading(view, request)
      }

      override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        setProgressDialogVisibility(true)
        super.onPageStarted(view, url, favicon)
      }

      override fun onPageFinished(view: WebView?, url: String?) {
        isLoaded = true
        setProgressDialogVisibility(false)
        super.onPageFinished(view, url)
      }

      override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
        isLoaded = false
        val errorMessage = "Got Error! $error"
        showToast(errorMessage)
        binding!!.infoTV.text = errorMessage
        setProgressDialogVisibility(false)
        super.onReceivedError(view, request, error)
      }


    }
  }

  private fun showToastToExit() {
    when {
      doubleBackToExitPressedOnce -> {
        showToast("Back Pressed")
      }
      else -> {
        doubleBackToExitPressedOnce = true
        showToast(getString(R.string.back_again_to_exit))
        Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
      }
    }
  }

  private fun setProgressDialogVisibility(visible: Boolean) {
    if (visible) progress = Progress(context, R.string.please_wait, cancelable = true)
    progress?.apply { if (visible) show() else dismiss() }
  }



  @RequiresApi(Build.VERSION_CODES.M)
  private fun isOnline(): Boolean {
    val connectivityManager =
      requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =

      connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    if (capabilities != null) {
      when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
          return true
        }
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
          return true
        }
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
          Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
          return true
        }
      }
    }
    return false
  }

  private fun showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
  }

  private fun showNoNetSnackBar() {
    val snack = Snackbar.make(binding!!.rootView, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
    snack.setAction(getString(R.string.settings)) {
      startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    }
    snack.show()
  }


  @RequiresApi(Build.VERSION_CODES.M)
  override fun onResume() {
    if (isOnline() && !isLoaded) loadWebView()
    super.onResume()
  }


}