package kids.youtube.channels.view

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.google.android.youtube.player.YouTubeApiServiceUtil
import com.google.android.youtube.player.YouTubeInitializationResult
import kids.youtube.channels.R
import kids.youtube.channels.model.Video
import kids.youtube.channels.view.adapter.ChannelAdapter

const val EXTRA_MESSAGE = "kids.youtube.channels.view.VideoId"

class MainActivity : AppCompatActivity(), ChannelAdapter.VideoSelectedListener {

    override fun onVideoSelected(video: Video) {
//        val channelVideo = ChannelVideo.newInstance(video.videoId)
//        changeFragment(channelVideo)


        val intent = Intent(this, ActivityChannelVideo::class.java).apply {
            putExtra(EXTRA_MESSAGE, video.videoId)
        }
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkYouTubeApi()
        AndroidNetworking.initialize(getApplicationContext())

        changeFragment(FragmentSearchChannel.newInstance())

    }

    private fun changeFragment(fragment: Fragment){
        if (fragment == null) {
            return
        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.name).commit()
    }

    private fun checkYouTubeApi() {
        val errorReason = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this)
        if (errorReason.isUserRecoverableError) {
            errorReason.getErrorDialog(this, 1).show()
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            val errorMessage = String.format(getString(R.string.error_player),
                    errorReason.toString())
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}
