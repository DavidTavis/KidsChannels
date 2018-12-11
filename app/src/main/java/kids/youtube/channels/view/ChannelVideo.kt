package kids.youtube.channels.view

import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kids.youtube.channels.R

class ChannelVideo : Fragment(){

    lateinit var videoId: String
    private var youTubePlayerFragment: FragmentVideo? = null
    companion object {
        fun newInstance(videoId: String) : ChannelVideo {
            val fragment = ChannelVideo()
            val args = Bundle()
            args.putString("videoId", videoId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoId = arguments!!.getString("videoId")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.channel_video_fragment, container, false)
//        val fragmentVideo = fragmentManager!!.findFragmentById(R.id.youtube_player_fragment) as FragmentVideo
//        fragmentVideo.setVideoId(videoId)
        initializeYoutubePlayer()

        youTubePlayerFragment!!.setVideoId(videoId)
        return view
    }

    private fun initializeYoutubePlayer() {

        youTubePlayerFragment = fragmentManager.findFragmentById(R.id.youtube_player_fragment) as? FragmentVideo
                ?: return

        youTubePlayerFragment!!.initialize(resources.getString(R.string.youtube_apikey), object : YouTubePlayer.OnInitializedListener {

            override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer,
                                                 wasRestored: Boolean) {
                if (!wasRestored) {
                    val youTubePlayer = player

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(videoId)
                }
            }

            override fun onInitializationFailure(arg0: YouTubePlayer.Provider, arg1: YouTubeInitializationResult) {

                //print or show error if initialization failed
                Log.e("mytag", "Youtube Player View initialization failed")
            }
        })

        youTubePlayerFragment!!.setVideoId(videoId)
    }

}
