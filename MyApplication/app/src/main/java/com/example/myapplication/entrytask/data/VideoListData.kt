package com.example.myapplication.entrytask.data

import com.google.gson.annotations.SerializedName


/**
 * author: beitingsu
 * created on: 2020/3/31 10:53 AM
 */
class VideoListData {
    @SerializedName("list")
    var mListData : MutableList<VideoData> ?= null

    @SerializedName("has_more")
    var mIsHasMore = false
}