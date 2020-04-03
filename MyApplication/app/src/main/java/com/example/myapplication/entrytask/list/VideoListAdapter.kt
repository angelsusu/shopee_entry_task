package com.example.myapplication.entrytask.list

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.entrytask.MyApplication
import com.example.myapplication.entrytask.data.VideoData
import com.example.myapplication.entrytask.utils.ExoPlayerManager
import com.example.myapplication.entrytask.utils.NetworkUtils
import com.example.myapplication.entrytask.utils.ToastUtils
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.SimpleExoPlayerView
import com.squareup.picasso.Picasso


/**
 * author: beitingsu
 * created on: 2020/3/31 12:09 PM
 */
class VideoListAdapter : BaseAdapter() {

    private var mDataList = mutableListOf<VideoData>()

    private var mLastPlayerView: View? = null
    private var mLastPos = 0

    companion object {
        private const val IMG_URL_PREFIX = "https://cf.shopee.co.id/file/"
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder: ViewHolder? = null
        var view: View? = null

        if (convertView == null) {
            view = View.inflate(MyApplication.getApplicationContext(), R.layout.video_view, null)
            holder = ViewHolder()
            holder.mTitle = view.findViewById(R.id.tv_video_title)
            holder.mVideoPlayerView = view.findViewById(R.id.player_view)
            holder.mCover = view.findViewById(R.id.iv_cover)
            holder.mLoadingText = view.findViewById(R.id.tv_loading)
            holder.mPlayBtn = view.findViewById(R.id.iv_play_btn)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val videoData = getItem(position) as? VideoData
        initView(holder, videoData, false)

        return view
    }

    override fun getItem(position: Int): Any? {
        if (position < 0 || position >= mDataList.size) {
            return null
        }
        return mDataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    private fun initView(holder: ViewHolder, videoData: VideoData?, isReset: Boolean) {
        if (holder.mVideoPlayerView?.player?.isPlaying == true && !isReset) {
            return
        }

        //title
        holder.mTitle?.text = videoData?.mTitle

        //cover
        holder.mCover?.visibility = View.VISIBLE
        if (!isReset) {
            val imgUrl = IMG_URL_PREFIX + videoData?.mCover
            Picasso.get().load(imgUrl)?.into(holder.mCover)
        }

        //loading
        holder.mPlayBtn?.visibility = View.VISIBLE
        holder.mLoadingText?.visibility = View.GONE

        //player view
        holder.mVideoPlayerView?.visibility = View.GONE
        holder.mPlayEventListener?.let {
            holder.mVideoPlayerView?.player?.removeListener(it)
        }
        holder.mVideoPlayerView?.player = null
    }


    private fun updatePlayerView(holder: ViewHolder, data: VideoData?) {
        resetPlayer()
        holder.mPlayBtn?.visibility = View.GONE
        holder.mLoadingText?.visibility = View.VISIBLE

        val player = ExoPlayerManager.getInstance().getPlayer()
        holder.mVideoPlayerView?.visibility = View.VISIBLE
        holder.mVideoPlayerView?.player = player

        player?.playWhenReady = true
        player?.seekTo(0, 0)

        data?.mUrl?.let {
            if (ExoPlayerManager.getInstance().getPlayData()?.mRecordId != data.mRecordId) {
                ExoPlayerManager.getInstance().setPlayData(data)
                ExoPlayerManager.createMediaSource(it)?.let { MediaSource ->
                    player?.prepare(MediaSource, false, true)
                }
            }
        }

        val listener= PlayEventListener(holder)
        holder.mPlayEventListener = listener
        player?.addListener(listener)
    }

    fun addData(isBottom: Boolean, listData: MutableList<VideoData>) {
        if (isBottom) {
            mDataList.addAll(listData)
        } else {
            mDataList.addAll(0, listData)
        }
    }

    fun playVideo(curView: View, pos: Int) {
        if (pos < 0 || pos >= mDataList.size) {
            return
        }
        val holder: ViewHolder = curView.tag as ViewHolder
        if (holder.mVideoPlayerView?.player?.isPlaying == true
            && holder.mVideoPlayerView?.player?.playWhenReady == true) {
            return
        }

        if (!NetworkUtils.isNetworkActive()) {
            ToastUtils.getInstance().showToast(MyApplication.getApplicationContext()?.
                getString(R.string.net_error))
            return
        }

        resetLastPlayerView()
        mLastPlayerView = curView
        mLastPos = pos

        val videoData =  getItem(pos) as? VideoData
        initView(holder, videoData, true)
        updatePlayerView(holder, videoData)
    }

    private fun resetLastPlayerView() {
        mLastPlayerView?.let {
            val holder = it.tag as ViewHolder
            resetPlayer()
            initView(holder, getItem(mLastPos) as? VideoData, true)
        }
    }

    private fun resetPlayer() {
        ExoPlayerManager.getInstance().resetPlayer()
    }

    fun unbindPlayer() {
        mLastPlayerView?.let {
            val holder = it.tag as ViewHolder
            holder.mVideoPlayerView?.player = null
        }
    }

    fun rebindPlayer() {
        mLastPlayerView?.let {
            val holder = it.tag as ViewHolder
            holder.mVideoPlayerView?.player = ExoPlayerManager.getInstance().getPlayer()
            holder.mVideoPlayerView?.player?.playWhenReady = true
        }
    }

    class ViewHolder {
        var mTitle: TextView? = null
        var mCover: ImageView? = null
        var mPlayBtn: ImageView? = null
        var mLoadingText: TextView? = null
        var mVideoPlayerView: SimpleExoPlayerView? = null

        var mPlayEventListener: PlayEventListener? = null
    }

    class PlayEventListener(holder: ViewHolder) : Player.EventListener {
        private val mHolder = holder

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            if (playbackState == Player.STATE_READY) {
                mHolder.mCover?.visibility = View.GONE
                mHolder.mLoadingText?.visibility = View.GONE
                mHolder.mPlayBtn?.visibility = View.GONE
            }
        }
    }
}