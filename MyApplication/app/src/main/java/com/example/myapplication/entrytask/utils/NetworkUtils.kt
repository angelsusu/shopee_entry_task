package com.example.myapplication.entrytask.utils

import android.content.Context
import android.net.ConnectivityManager
import com.example.myapplication.entrytask.MyApplication

/**
 * author: beitingsu
 * created on: 2020/3/31 1:29 PM
 */
class NetworkUtils {

    companion object {

        @JvmStatic
        fun isNetworkActive(): Boolean {
            val context = MyApplication.getApplicationContext()

            val manager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val networkInfo = manager?.getActiveNetworkInfo()

            networkInfo?.let {
                return it.isAvailable()
            }

            return false
        }
    }

}