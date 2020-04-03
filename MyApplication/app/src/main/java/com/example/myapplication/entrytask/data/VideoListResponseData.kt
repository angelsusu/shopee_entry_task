package com.example.myapplication.entrytask.data

import com.google.gson.annotations.SerializedName

/**
 * author: beitingsu
 * created on: 2020/3/31 10:55 AM
 */
class VideoListResponseData {

    @SerializedName("err_code")
    var mErrorCode = 0

    @SerializedName("err_msg")
    var mErrorMsg = ""

    @SerializedName("data")
    var mData : VideoListData ?= null
}