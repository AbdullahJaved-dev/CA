package com.logicielhouse.ca.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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
import com.logicielhouse.ca.BaseApplication
import com.logicielhouse.ca.R
import com.logicielhouse.ca.adapter.SongsAdapter
import com.logicielhouse.ca.model.SongModel
import com.logicielhouse.ca.utils.AppConstants
import com.logicielhouse.ca.utils.displayMessage
import kotlinx.android.synthetic.main.activity_songs.*
import kotlinx.android.synthetic.main.activity_songs.tvNoDataFound
import kotlinx.android.synthetic.main.fragment_videos.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SongsFragment : Fragment(R.layout.fragment_songs), SongsAdapter.SongsAdapterClickListeners,
    Player.EventListener {
    private lateinit var songsAdapterClickListeners: SongsAdapter.SongsAdapterClickListeners
    private lateinit var songsAdapter: SongsAdapter
    private var songsList = ArrayList<SongModel>()
    private var currentSong: String? = null


    //Exo Player
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0L
    private val bandwidthMeter = DefaultBandwidthMeter()
    private val loadControl = DefaultLoadControl()
    private var trackSelector = DefaultTrackSelector(
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songsAdapterClickListeners = this
        songsAdapter = SongsAdapter(songsAdapterClickListeners, songsList)

        rvSongs.apply {
            adapter = songsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        loadSongs()
    }

    private fun loadSongs() {
        val songsRequest = object :
            JsonObjectRequest(Method.GET, AppConstants.GET_SOURCES, null, { response ->
                try {
                    songsList.clear()
                    val songsArray: JSONArray = response.getJSONArray("sources")
                    var i = 0
                    while (i < songsArray.length()) {
                        val songsObject: JSONObject = songsArray.getJSONObject(i)
                        val songModel = SongModel(
                            songsObject.optInt("source_id"),
                            songsObject.optString("title"),
                            songsObject.getString("thumbnailUri"),
                            songsObject.getString("source_link"),
                        )
                        songsList.add(songModel)
                        i++
                    }
                    songsAdapter.notifyDataSetChanged()
                    if (songsProgressBar != null) {
                        songsProgressBar.visibility = View.GONE
                        if (songsList.size == 0) {
                            tvNoDataFound.visibility = View.VISIBLE
                        } else {
                            tvNoDataFound.visibility = View.GONE
                        }
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, { error ->
                try {
                    videosProgressBar.visibility = View.GONE
                    val response: NetworkResponse = error.networkResponse
                    if (response.data != null) {
                        val errorObj = JSONObject(String(response.data))
                        displayMessage(requireActivity(), errorObj.optString("message"))
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "getAllVideos: " + e.localizedMessage)
                    displayMessage(requireContext(), getString(R.string.unknown_error))
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val pref =
                    requireActivity().getSharedPreferences(
                        BaseApplication.PREFS,
                        Context.MODE_PRIVATE
                    )
                headers["type"] = "songs"
                headers["languages-code"] = pref.getString(BaseApplication.LOCALE, "ar") as String
                return headers
            }

        }
        songsRequest.retryPolicy =
            DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        val requestQueue: RequestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(songsRequest)
    }

    override fun playSong(songModel: SongModel) {
        if (audioPlayer.visibility == View.GONE) {
            audioPlayer.visibility = View.VISIBLE
        }
        if (player != null) {
            releasePlayer(0L, 0)
        }
        currentSong = songModel.songURI
        initializePlayer()
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(requireContext()),
            trackSelector, loadControl
        )
        val mediaSource: MediaSource?
        val uri = Uri.parse(currentSong)
        mediaSource = buildMediaSource(uri)

        player?.playWhenReady = true
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource, false, false)
        audioPlayer.player = player

        player?.addListener(this)
    }

    private fun releasePlayer(
        playbackPos: Long = 1L,
        currWindowIndex: Int = 1
    ) {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            if (playbackPos == 0L && currWindowIndex == 0) {
                playbackPosition = 0L
                currentWindow = 0
            } else {
                playbackPosition = player!!.currentPosition
                currentWindow = player!!.currentWindowIndex
            }
            player?.release()
            player = null
        }
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24 && currentSong != null) {
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
        if ((Util.SDK_INT < 24 || player == null) && currentSong != null) {
            initializePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(requireContext(), "exoplayer-driver", bandwidthMeter)
        return ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
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
        if (audioPlayer.visibility == View.VISIBLE) {
            Toast.makeText(
                requireActivity(),
                "" + error.sourceException.message,
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    override fun onPositionDiscontinuity(reason: Int) {

    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onSeekProcessed() {

    }


}