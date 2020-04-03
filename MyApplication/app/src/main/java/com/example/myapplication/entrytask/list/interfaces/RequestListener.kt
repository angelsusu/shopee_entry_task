package com.example.myapplication.entrytask.list.interfaces

/**
 * author: beitingsu
 * created on: 2020/3/31 3:34 PM
 */
interface RequestListener<T> {
    fun onSuccess(responseData: T?)
    fun onFail()
}