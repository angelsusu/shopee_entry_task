package com.example.myapplication.entrytask.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.entrytask.data.VideoData
import com.example.myapplication.entrytask.utils.BaseVideoPlayActivity
import com.example.myapplication.entrytask.utils.ExoPlayerManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.common_head_layout.*
import kotlinx.android.synthetic.main.video_view.*

/**
 * author: beitingsu
 * created on: 2020/3/31 5:44 PM
 */
class VideoDetailActivity : BaseVideoPlayActivity() {

    private var mPlayEventListener = PlayEventListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_detail_layout)

        init()
    }

    private fun init() {
        initHeadView()
        initVideoPlayView()
    }

    private fun initHeadView() {
        tv_title?.text = getString(R.string.video_detail_title)
        tv_sub?.setOnClickListener {
            finish()
        }
    }

    private fun initVideoPlayView() {
        initVideoPlayLayoutHeight()
        val videoData = intent.getSerializableExtra(DATA_KEY) as VideoData
        tv_video_title?.text = videoData.mTitle

        iv_play_btn?.visibility = View.GONE

        if (ExoPlayerManager.getInstance().getPlayData()?.mRecordId != videoData.mRecordId) {
            hidePlayerView(videoData)
            initExoplayer(videoData)
        } else {
            if (ExoPlayerManager.getInstance().getPlayer()?.isPlaying == false) {
                hidePlayerView(videoData)
                ExoPlayerManager.getInstance().getPlayer()?.addListener(mPlayEventListener)
            } else {
               showPlayerView()
            }
            //player view
            player_view?.player = ExoPlayerManager.getInstance().getPlayer()
        }
        ExoPlayerManager.getInstance().setPlayData(videoData)
    }

    private fun initVideoPlayLayoutHeight() {
        rl_video?.layoutParams?.height = FrameLayout.LayoutParams.MATCH_PARENT
    }

    private fun hidePlayerView(videoData: VideoData) {
        iv_cover?.visibility = View.VISIBLE
        val imgUrl = IMG_URL_PREFIX + videoData.mCover
        Picasso.get().load(imgUrl)?.into(iv_cover)

        //loading
        tv_loading?.visibility = View.VISIBLE

        //player view
        player_view?.visibility = View.GONE
        player_view?.player = null
    }

    private fun showPlayerView() {
        iv_cover?.visibility = View.GONE
        tv_loading?.visibility = View.GONE
        iv_play_btn?.visibility = View.GONE

        player_view?.visibility = View.VISIBLE
    }

    private fun initExoplayer(videoData: VideoData) {
        val player = ExoPlayerManager.getInstance().getPlayer()
        createMediaSource(videoData.mUrl)?.let {
            player?.prepare(it)
        }
        player_view?.player = player
        player?.playWhenReady = true
        player?.seekTo(0, 1000)
        player?.addListener(mPlayEventListener)
    }

    private fun createMediaSource(videoUrl: String): MediaSource? {
        return ExoPlayerManager.createMediaSource(videoUrl)
    }

    override fun onDestroy() {
        super.onDestroy()
        player_view?.player = null
        ExoPlayerManager.getInstance().getPlayer()?.removeListener(mPlayEventListener)
    }

    inner class PlayEventListener : Player.EventListener {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                showPlayerView()
            }
        }
    }


    companion object {

        private const val DATA_KEY = "video_data"
        private const val IMG_URL_PREFIX = "https://cf.shopee.co.id/file/"

        @JvmStatic
        fun startVideoDetailActivity(activity: AppCompatActivity, videoData: VideoData) {
            val intent = Intent(activity, VideoDetailActivity::class.java)
            intent.putExtra(DATA_KEY, videoData)
            activity.startActivity(intent)
        }
    }
}