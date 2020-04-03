package com.example.myapplication.entrytask.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.entrytask.MyApplication
import com.example.myapplication.entrytask.data.VideoListData
import com.example.myapplication.entrytask.data.VideoListResponseData
import com.example.myapplication.entrytask.list.interfaces.RequestListener
import com.example.myapplication.entrytask.utils.NetworkUtils
import com.example.myapplication.entrytask.utils.ToastUtils

/**
 * author: beitingsu
 * created on: 2020/3/31 10:11 AM
 */
class VideoListViewModel : ViewModel() {

    private var mOffset = 0
    private var mIsCanLoadMore = false

    //包裹要检测的数据
    private val mVideoListData: MutableLiveData<VideoListData> = MutableLiveData()

    private var mRepository: VideoListRepository = VideoListRepository()

    private fun getVideoList() {
        mRepository.getVideoList(mOffset, REQUEST_LIMIT, object :
            RequestListener<VideoListResponseData> {
            override fun onSuccess(responseData: VideoListResponseData?) {
                responseData?.let {
                    mIsCanLoadMore = it.mData?.mIsHasMore ?: false
                    mVideoListData.value = it.mData
                    ++mOffset
                    return
                }
                mVideoListData.value = null
            }

            override fun onFail() {
                if (!NetworkUtils.isNetworkActive()) {
                    ToastUtils.getInstance().showToast(MyApplication.getApplicationContext()?.getString(
                        R.string.net_error))
                } else {
                    ToastUtils.getInstance().showToast(MyApplication.getApplicationContext()?.getString(
                        R.string.request_fail))
                }
                mVideoListData.value = null
            }
        })
    }

    fun refresh() {
        mOffset = 0
        getVideoList()
    }

    fun loadMore() {
        Log.d(TAG, "loadMore:offset:$mOffset")
        getVideoList()
    }

    fun getLiveData(): MutableLiveData<VideoListData> {
        return mVideoListData
    }

    fun initData() {
        mVideoListData.value = mRepository.getLocalCache()
    }

    fun clearCache() {
        mRepository.clearCache()
    }

    companion object {
        private val TAG = VideoListViewModel::class.java.simpleName
        private const val REQUEST_LIMIT = 10
    }

}