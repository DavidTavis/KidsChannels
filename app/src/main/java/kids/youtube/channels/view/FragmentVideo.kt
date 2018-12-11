package kids.youtube.channels.view

import android.os.Bundle

import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import kids.youtube.channels.R

class FragmentVideo : YouTubePlayerFragment(), YouTubePlayer.OnInitializedListener {

    private var player: YouTubePlayer? = null
    private var videoId: String? = null

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        initialize(resources.getString(R.string.youtube_apikey), this)
    }

    override fun onDestroy() {
        if (player != null) {
            player!!.release()
        }
        super.onDestroy()
    }

    fun setVideoId(videoId: String?) {
        if (videoId != null && videoId != this.videoId) {
            this.videoId = videoId
            if (player != null) {
                player!!.cueVideo(videoId)
            }
        }
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, restored: Boolean) {

        this.player = player
//        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE)
//        player.setOnFullscreenListener(getActivity() as YouTubePlayer.OnFullscreenListener)

        if (!restored && videoId != null) {
            player.cueVideo(videoId)
        }

    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider, result: YouTubeInitializationResult) {

        this.player = null

    }

    fun backnormal() {
        player!!.setFullscreen(false)
    }
}