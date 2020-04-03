package com.example.myapplication.entrytask.list

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.entrytask.data.VideoData
import com.example.myapplication.entrytask.data.VideoListData
import com.example.myapplication.entrytask.detail.VideoDetailActivity
import com.example.myapplication.entrytask.utils.BaseVideoPlayActivity
import com.example.myapplication.entrytask.utils.ExoPlayerManager
import com.example.myapplication.entrytask.utils.ToastUtils
import com.shopee.szlanding.SZDemoActivity
import kotlinx.android.synthetic.main.common_head_layout.*
import kotlinx.android.synthetic.main.common_tip_layout.*
import kotlinx.android.synthetic.main.footer_layout.*
import kotlinx.android.synthetic.main.video_list_layout.*


/**
 * author: beitingsu
 * created on: 2020/3/31 5:43 PM
 */
class VideoListActivity : BaseVideoPlayActivity() {

    private var mAdapter: VideoListAdapter? = null
    private var mViewModel: VideoListViewModel? = null

    private var mStatus = INIT_STATUS

    private var mFirstVisible = 0
    private var mVisibleCount = 0

    private var mFooterView: View? = null


    companion object {
        private var INIT_STATUS = 0
        private var REFRESH_STATUS = 1
        private const val LOAD_MORE_STATUS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_list_layout)

        init()
    }

    private fun init() {
        initHeadView()
        initErrorView()
        initListView()
        initViewModel()
        initData()
    }

    private fun initErrorView() {
        tv_content?.setOnClickListener {
            refreshData()
        }
    }

    private fun initHeadView() {
        tv_sub?.text = getString(R.string.jump_text)
        tv_title?.text = getString(R.string.video_list_title)
        tv_clear?.visibility = View.VISIBLE

        tv_sub?.setOnClickListener {
            jumpSZDemoActivity()
        }
        tv_clear?.setOnClickListener {
            mViewModel?.clearCache()
        }
    }

    private fun initListView() {
        mAdapter = VideoListAdapter()
        lv_content?.adapter = mAdapter
        lv_content.setOnItemClickListener { _, view, position, _ ->
            onItemClick(view, position)
        }
        lv_content?.setOnScrollListener(ScrollListener())
        mFooterView = View.inflate(this, R.layout.footer_layout, null)
        lv_content?.addFooterView(mFooterView)
        mFooterView?.setOnClickListener {
            tv_tip?.text = getString(R.string.loading_text)
            loadMore()
        }

        rf_layout?.setOnRefreshListener {
            refreshData()
        }
    }

    private fun initViewModel() {
        mViewModel = VideoListViewModel()
        mViewModel?.getLiveData()?.observe(this, Observer {
            updateView(it)
            if (it?.mListData == null) {
                handleNoData()
            }
        })
    }

    private fun onItemClick(view: View, position:Int) {
        mAdapter?.getItem(position)?.let {
            val videoData = it as VideoData
            if (ExoPlayerManager.getInstance().getPlayData()?.mRecordId != videoData.mRecordId) {
                mAdapter?.playVideo(view, position)
                mAdapter?.unbindPlayer()
            } else {
                mAdapter?.unbindPlayer()
            }
            jumpVideoDetailActivity(videoData)
        }
    }

    private fun updateView(videoListData: VideoListData?) {
        rf_layout?.isRefreshing = false
        videoListData?.mListData?.let { listData ->
            when (mStatus) {
                INIT_STATUS -> {
                    mAdapter?.addData(false, listData)
                }
                REFRESH_STATUS -> {
                    mAdapter?.addData(false, listData)
                    ToastUtils.getInstance().showToast(getString(R.string.refresh_success))
                }
                LOAD_MORE_STATUS -> {
                    tv_tip?.text = getString(R.string.load_more_text)
                    mAdapter?.addData(true, listData)
                }
            }
            common_tip_layout?.visibility = View.GONE
            mAdapter?.notifyDataSetChanged()
        }
    }

    private fun handleNoData() {
        when (mStatus) {
            INIT_STATUS -> {
                common_tip_layout?.visibility = View.VISIBLE
                tv_content?.text = getString(R.string.net_error)
            }
            REFRESH_STATUS -> {
                val dataCount = mAdapter?.count ?: 0
                if (dataCount <= 0) {
                    common_tip_layout?.visibility = View.VISIBLE
                    tv_content?.text = getString(R.string.net_error)
                }
            }
            LOAD_MORE_STATUS -> {
                tv_tip?.text = getString(R.string.net_error)
            }
        }
    }

    private fun initData() {
        mViewModel?.initData()
    }

    private fun refreshData() {
        mStatus = REFRESH_STATUS
        mViewModel?.refresh()
    }

    private fun loadMore() {
        mStatus = LOAD_MORE_STATUS
        mViewModel?.loadMore()
    }

    private fun jumpSZDemoActivity() {
        val intent = Intent(this, SZDemoActivity::class.java)
        startActivity(intent)
    }

    private fun jumpVideoDetailActivity(videoData: VideoData) {
        VideoDetailActivity.startVideoDetailActivity(this, videoData)
    }

    inner class ScrollListener : AbsListView.OnScrollListener {
        override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int,
                              totalItemCount: Int) {

            mFirstVisible = firstVisibleItem
            mVisibleCount = visibleItemCount
        }

        override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            when (scrollState) {
                AbsListView.OnScrollListener.SCROLL_STATE_IDLE -> {
                    view?.let {
                        autoPlayVideo()
                    }
                }
            }
        }
    }

    private fun autoPlayVideo() {
        for (index in 0 .. mVisibleCount) {
            val itemView = lv_content?.getChildAt(index)

            itemView?.let {
                val rect = Rect()
                it.getGlobalVisibleRect(rect)

                val height = it.height

                if (rect.top >= 0 && rect.bottom >= height) {
                    mAdapter?.playVideo(it, index + mFirstVisible)
                    return
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mAdapter?.rebindPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        ExoPlayerManager.getInstance().releasePlayer()
    }
}