package com.logicielhouse.ca.ui

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.logicielhouse.ca.R
import com.logicielhouse.ca.utils.locale.LocaleManager
import kotlinx.android.synthetic.main.activity_video.*


class VideoActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    private var stopPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.app_video)
        videoView.setVideoURI(uri)
        videoView.setMediaController(null)
        videoView.start()
        videoView.setOnCompletionListener(this)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        p0?.stop()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onPause() {
        super.onPause()
        stopPosition = videoView.currentPosition
        videoView.pause()
    }

    override fun onResume() {
        super.onResume()
        videoView.seekTo(stopPosition)
        videoView.start()
    }

}