package kids.youtube.channels.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kids.youtube.channels.R

class ActivityChannelVideo: AppCompatActivity() {

    private var videoId: String? = null
    private val TAG = "mytag"
    private val recyclerView: RecyclerView? = null

    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    private val youtubeVideoArrayList: ArrayList<String>? = null
    private var youTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.channel_video_fragment)
        videoId = intent.extras.getString(EXTRA_MESSAGE)
//        generateDummyVideoList();
        initializeYoutubePlayer();
//        setUpRecyclerView();
//        populateRecyclerView();
    }

    private fun initializeYoutubePlayer() {

        youTubePlayerFragment = supportFragmentManager
                .findFragmentById(R.id.youtube_player_fragment) as YouTubePlayerSupportFragment

        if (youTubePlayerFragment == null)
            return

        youTubePlayerFragment!!.initialize(resources.getString(R.string.youtube_apikey), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer,
                                                 wasRestored: Boolean) {
                if (!wasRestored) {
                    youTubePlayer = player

                    //set the player style default
                    youTubePlayer!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)

                    //cue the 1st video by default
                    youTubePlayer!!.cueVideo(videoId)
                }
            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed")
            }
        })
    }

}