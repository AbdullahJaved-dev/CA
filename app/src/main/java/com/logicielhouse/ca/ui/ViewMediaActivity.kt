package com.logicielhouse.ca.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.logicielhouse.ca.R
import com.logicielhouse.ca.model.PicturesModel
import com.logicielhouse.ca.model.VideosModel
import com.logicielhouse.ca.utils.SessionManager
import com.logicielhouse.ca.utils.locale.LocaleManager
import com.logicielhouse.ca.utils.setTextHTML
import kotlinx.android.synthetic.main.activity_view_media.*


class ViewMediaActivity : AppCompatActivity(), Player.EventListener {

    private lateinit var toolbar: Toolbar
    private var videoURI = ""
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private val bandwidthMeter = DefaultBandwidthMeter()
    private val loadControl = DefaultLoadControl()
    private var trackSelector = DefaultTrackSelector(
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    )

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_media)
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        if (intent != null) {
            if (intent.getParcelableExtra<PicturesModel>("picture") != null) {
                val picturesModel = intent.getParcelableExtra<PicturesModel>("picture")
                setupPictureUI(picturesModel!!)
            } else {
                val videosModel = intent.getParcelableExtra<VideosModel>("video")
                setupVideoUI(videosModel!!)
            }
        }
        loadAd()

    }

    private fun loadAd() {
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.adMobInterstitialAddId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                Log.d("TAG", "onAdLoaded: Add Loaded ")
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.e("TAG", "onAdFailedToLoad: ${adError.message + adError.cause}")
                finish()
            }

            override fun onAdOpened() {
                Log.d("TAG", "onAdOpened: ")
            }

            override fun onAdClicked() {
                Log.d("TAG", "onAdClicked: ")
            }

            override fun onAdLeftApplication() {
                Log.d("TAG", "onAdLeftApplication: Left Application on Ad")
            }

            override fun onAdClosed() {
                finish()
            }
        }
    }

    private fun setupVideoUI(videosModel: VideosModel) {
        ivPlayBtn.visibility = View.VISIBLE
        Glide.with(this).load(videosModel.videoThumbnail).into(ivMediaThumbnail)
        tvMediaDate.text = videosModel.videoDate
        tvMediaTitle.text = videosModel.videoTitle
        if (videosModel.videoDescription == "null") {
            tvMediaDescription.text = ""
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvMediaDescription.text =
                    Html.fromHtml(videosModel.videoDescription, Html.FROM_HTML_MODE_COMPACT)
            } else {
                tvMediaDescription.text = setTextHTML(videosModel.videoDescription)
            }
        }
        videoURI = videosModel.videoURL
        exoplayerView.visibility = View.VISIBLE
        initializePlayer()
        ivMediaThumbnail.visibility = View.GONE
        ivPlayBtn.visibility = View.GONE
    }

    private fun setupPictureUI(picturesModel: PicturesModel) {
        Glide.with(this).load(picturesModel.pictureURI).into(ivMediaThumbnail)
        tvMediaDate.text = picturesModel.pictureDate
        tvMediaTitle.text = picturesModel.pictureTitle
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvMediaDescription.text =
                Html.fromHtml(picturesModel.pictureDescription, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvMediaDescription.text = setTextHTML(picturesModel.pictureDescription)
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val showCount: Int = SessionManager.getInstance(this)?.getIntPref("c")!!
        if (showCount <= 2) {
            SessionManager.getInstance(this)?.setIntPref("c", showCount + 1)
            finish()
        } else {
            SessionManager.getInstance(this)?.setIntPref("c", 0)
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.")
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun initializePlayer() {

        player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            trackSelector, loadControl
        )
        exoplayerView.player = player

        val uri = Uri.parse(videoURI)
        val mediaSource = buildMediaSource(uri)
        player?.playWhenReady = false
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)

        player?.addListener(this)
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, "exoplayer-ca", bandwidthMeter)
        return ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {

    }

    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            progressBar.visibility = View.GONE
    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {
        when (error!!.type) {
            ExoPlaybackException.TYPE_SOURCE -> Log.e(
                "TAG",
                "TYPE_SOURCE: " + error.sourceException.message
            )
            ExoPlaybackException.TYPE_RENDERER -> Log.e(
                "TAG",
                "TYPE_RENDERER: " + error.rendererException.message
            )
            ExoPlaybackException.TYPE_UNEXPECTED -> Log.e(
                "TAG",
                "TYPE_UNEXPECTED: " + error.unexpectedException.message
            )
        }
        try {
            if (exoplayerView.visibility == View.VISIBLE) {
                Toast.makeText(this, "" + error.sourceException.message, Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.stackTrace
        }

    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }

}