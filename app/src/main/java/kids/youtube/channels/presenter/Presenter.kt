package kids.youtube.channels.presenter

import kids.youtube.channels.model.Channel
import kids.youtube.channels.model.Video

interface Presenter {
    fun onStop()
    fun onShow(channels: List<Channel>)
    fun onShowEmptyList()
    fun onShowMoreVideo(videos: List<Video>)
    fun onShowMoreChannel(channels: List<Channel>)
    fun onShowProgress()
    fun onHideProgress()
}