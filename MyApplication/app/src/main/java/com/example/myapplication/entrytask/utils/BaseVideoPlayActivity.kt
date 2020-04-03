package com.example.myapplication.entrytask.utils

import androidx.appcompat.app.AppCompatActivity

/**
 * author: beitingsu
 * created on: 2020/4/2 5:41 PM
 */
abstract class BaseVideoPlayActivity : AppCompatActivity() {

    override fun onPause() {
        super.onPause()
        ExoPlayerManager.getInstance().getPlayer()?.playWhenReady = false
    }

    override fun onResume() {
        super.onResume()
        ExoPlayerManager.getInstance().getPlayer()?.playWhenReady = true
    }
}