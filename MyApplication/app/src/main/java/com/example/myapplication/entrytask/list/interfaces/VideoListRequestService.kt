package com.example.myapplication.entrytask.list.interfaces

import com.example.myapplication.entrytask.data.VideoListResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * author: beitingsu
 * created on: 2020/3/31 3:09 PM
 */
interface VideoListRequestService {

    @GET("api/v1/homepage/replay/")
    fun getVideoList(@Query("offset") offset: Int, @Query("limit") limit: Int): Call<VideoListResponseData>
}