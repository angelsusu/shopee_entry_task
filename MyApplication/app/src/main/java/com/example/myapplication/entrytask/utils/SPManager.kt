package com.example.myapplication.entrytask.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.entrytask.MyApplication


/**
 * author: beitingsu
 * created on: 2020/3/31 1:30 PM
 */
class SPManager private constructor() {

    private var mSp: SharedPreferences? = null

    companion object {
        private const val CACHE_FILE_NAME = "cache_file"

        @JvmStatic
        fun getInstance(): SPManager {
            return SingletonHolder.holder
        }
    }

    init {
        mSp = MyApplication.getApplicationContext()
            ?.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE)
    }


    private object SingletonHolder {
        val holder = SPManager()
    }

    fun putString(key: String, value: String) {
        mSp?.edit()?.let {
            it.putString(key, value)
            it.commit()
        }
    }

    fun getString(key: String): String? {
        mSp?.let {
            return it.getString(key, "")
        }
        return null
    }

    fun putInt(key: String, value: Int) {
        mSp?.edit()?.let {
            it.putInt(key, value)
            it.commit()
        }
    }

    fun getInt(key: String): Int {
        mSp?.let {
            return it.getInt(key, 0)
        }
        return 0
    }
}