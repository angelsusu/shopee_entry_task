package com.example.myapplication.entrytask

import android.app.Application
import android.content.Context
import com.example.myapplication.entrytask.utils.ToastUtils

/**
 * author: beitingsu
 * created on: 2020/3/31 2:37 PM
 */
class MyApplication : Application() {

    companion object {
        private var mContext: Context? = null

        fun getApplicationContext() : Context? {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        ToastUtils.getInstance().init()
    }
}