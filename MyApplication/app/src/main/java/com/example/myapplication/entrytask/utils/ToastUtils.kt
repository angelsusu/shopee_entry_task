package com.example.myapplication.entrytask.utils

import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import com.example.myapplication.entrytask.MyApplication


/**
 * author: beitingsu
 * created on: 2020/3/31 3:45 PM
 */
class ToastUtils private constructor() {

    private var mHandler: Handler? = null

    companion object {

        @JvmStatic
        fun getInstance(): ToastUtils {
            return SingletonHolder.holder
        }
    }

    private object SingletonHolder {
        val holder = ToastUtils()
    }

    fun init() {
        mHandler = Handler()
    }

    fun showToast(text: String?) {
        mHandler?.post {
            val toast =
                Toast.makeText(MyApplication.getApplicationContext(), text, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
}