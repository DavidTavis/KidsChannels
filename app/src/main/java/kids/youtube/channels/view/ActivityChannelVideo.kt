package kids.youtube.channels.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.marshalchen.ultimaterecyclerview.RecyclerItemClickListener
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView
import kids.youtube.channels.R
import kids.youtube.channels.model.Video
import kids.youtube.channels.view.adapter.VideoAdapter


class ActivityChannelVideo: AppCompatActivity() {

    private var videoId: String? = null
    private val TAG = "mytag"
    private var recyclerView: UltimateRecyclerView? = null

    private var youTubePlayerFragment: YouTubePlayerSupportFragment? = null
    lateinit var youtubeVideoArrayList: ArrayList<Video>
    private var youTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.channel_video_fragment)

        videoId = intent.extras.getString(EXTRA_MESSAGE_VIDEO_ID)
        youtubeVideoArrayList = intent.getParcelableArrayListExtra(EXTRA_MESSAGE_VIDEO_LIST)

        initializeYoutubePlayer()
        setUpRecyclerView()
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

    private fun setUpRecyclerView() {
        recyclerView = findViewById(R.id.video_list)

        recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = VideoAdapter(youtubeVideoArrayList, context)
            setHasFixedSize(true)
        }
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(this, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: android.view.View?, position: Int) {
                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
//                    recyclerView!!.adapter.setSelectedPosition(position)

                    youTubePlayer!!.cueVideo(youtubeVideoArrayList[position].videoId)
                }
            }
        }))
    }


}