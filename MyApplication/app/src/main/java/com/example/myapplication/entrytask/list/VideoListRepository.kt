package com.example.myapplication.entrytask.list

import com.example.myapplication.entrytask.data.VideoData
import com.example.myapplication.entrytask.data.VideoListData
import com.example.myapplication.entrytask.data.VideoListResponseData
import com.example.myapplication.entrytask.list.interfaces.RequestListener
import com.example.myapplication.entrytask.list.interfaces.VideoListRequestService
import com.example.myapplication.entrytask.utils.RetrofitFactory
import com.example.myapplication.entrytask.utils.SPManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * author: beitingsu
 * created on: 2020/3/31 3:30 PM
 */
class VideoListRepository {

    fun getVideoList(offset: Int, limit: Int,
                     requestListener: RequestListener<VideoListResponseData>) {

        val requestService =
            RetrofitFactory.getRetrofit().create(VideoListRequestService::class.java)
        val call = requestService.getVideoList(offset, limit)

        call.enqueue(object : Callback<VideoListResponseData> {
            override fun onFailure(call: Call<VideoListResponseData>, t: Throwable) {
                requestListener.onFail()
            }

            override fun onResponse(call: Call<VideoListResponseData>,
                                    response: Response<VideoListResponseData>) {
                response.body()?.let {
                    if (it.mErrorCode == 0) {
                        requestListener.onSuccess(it)
                        updateLocalCache(it.mData)
                    } else {
                        requestListener.onFail()
                    }
                }
            }
        })
    }

    private fun updateLocalCache(listData: VideoListData?) {
        listData?.mListData?.let {
            for (index in 0 until  it.size) {
                SPManager.getInstance().putString(CACHE_DATA_KEY + index, Gson().toJson(it[index]))
            }
            SPManager.getInstance().putInt(CACHE_SIZE_KEY, it.size)
        }
    }

    fun getLocalCache(): VideoListData? {
        val cacheCount = SPManager.getInstance().getInt(CACHE_SIZE_KEY)
        if (cacheCount <= 0) {
            return null
        }

        val videoListData = VideoListData()
        val listData = mutableListOf<VideoData>()
        for (index in 0 until cacheCount) {
            val value = SPManager.getInstance().getString(CACHE_DATA_KEY + index)
            try {
                val videoData = Gson().fromJson(value, VideoData::class.java)
                listData.add(videoData)
            } catch (exception: JsonSyntaxException) {
                exception.printStackTrace()
            }
        }

        if (listData.isNotEmpty()) {
            videoListData.mListData = listData
        }

        if (videoListData.mListData.isNullOrEmpty()) {
            return null
        }

        return videoListData
    }

    fun clearCache() {
        SPManager.getInstance().putInt(CACHE_SIZE_KEY, 0)
    }

    companion object {
        private const val CACHE_DATA_KEY = "video_data_key_"
        private const val CACHE_SIZE_KEY = "cache_size"
    }
}