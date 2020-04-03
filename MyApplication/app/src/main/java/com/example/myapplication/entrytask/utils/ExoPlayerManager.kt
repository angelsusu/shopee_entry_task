package com.example.myapplication.entrytask.utils

import androidx.core.net.toUri
import com.example.myapplication.entrytask.MyApplication
import com.example.myapplication.entrytask.data.VideoData
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * author: beitingsu
 * created on: 2020/4/1 5:07 PM
 */
class ExoPlayerManager private constructor() {

    private var mPlayer: SimpleExoPlayer? = null
    private var mCurPlayData: VideoData? = null

    companion object {
        @JvmStatic
        fun getInstance(): ExoPlayerManager {
            return SingletonHolder.holder
        }

        @JvmStatic
        fun createMediaSource(videoUrl: String): MediaSource? {
            val context = MyApplication.getApplicationContext()
            context?.let {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    it,
                    Util.getUserAgent(it, "MyApplication"), null
                )
                return HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUrl.toUri())
            }
            return null
        }
    }

    private object SingletonHolder {
        val holder = ExoPlayerManager()
    }

    init {
        MyApplication.getApplicationContext()?.let {
            mPlayer = SimpleExoPlayer.Builder(it).build()
        }
    }

    fun getPlayer(): SimpleExoPlayer? {
        return mPlayer
    }

    fun setPlayData(videoData: VideoData?) {
        mCurPlayData = videoData
    }

    fun getPlayData(): VideoData? {
        return mCurPlayData
    }

    fun resetPlayer() {
        mPlayer?.playWhenReady = false
    }

    fun releasePlayer() {
        mPlayer?.release()
    }
}