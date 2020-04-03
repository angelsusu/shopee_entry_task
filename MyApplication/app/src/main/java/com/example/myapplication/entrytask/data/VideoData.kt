package com.example.myapplication.entrytask.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * author: beitingsu
 * created on: 2020/3/31 10:39 AM
 */
class VideoData : Serializable {

    @SerializedName("record_id")
    var mRecordId = 0

    @SerializedName("session_id")
    var mSessionId = 0

    @SerializedName("title")
    var mTitle = ""

    @SerializedName("uid")
    var mUid = 0

    @SerializedName("shop_id")
    var shopId = 0

    @SerializedName("username")
    var mUsername = ""

    @SerializedName("avatar")
    var mAvatar = ""

    @SerializedName("cover")
    var mCover = ""

    @SerializedName("view_count")
    var mViewCount = ""

    @SerializedName("session_mem_count")
    var mSessionMemCount = 0

    @SerializedName("url")
    var mUrl = ""
}