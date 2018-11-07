package kids.youtube.channels.presenter

import kids.youtube.channels.model.Channel

interface Presenter {
    fun onStop()
    fun onShow(channels: List<Channel>)
    fun onSetPageToken(pageToken: String)
}