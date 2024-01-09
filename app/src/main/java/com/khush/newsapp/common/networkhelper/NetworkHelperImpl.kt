package com.khush.newsapp.common.networkhelper

import android.content.Context
import android.net.ConnectivityManager
import com.khush.newsapp.common.networkhelper.NetworkHelper

class NetworkHelperImpl(
    private val context: Context
) : NetworkHelper {
    override fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }
}